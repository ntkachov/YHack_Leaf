#include <pebble.h>
#include "acceleromonitor.h"

#define HISTORY_MAX 144
#define MAX_ACCEL 4000
//#define LOG_PRINT_ACCEL 25
//#define TARGET_PEAKS = 4; 


static Window *window;
static TextLayer *text_layer;

//acceleromter fields
static AppTimer *timer;

int timer_frequency = 100;
bool running = false;
static int sample_window = 30;
static int last_x = 0;
static AccelData history[HISTORY_MAX];

static void select_click_handler(ClickRecognizerRef recognizer, void *context) {
  text_layer_set_text(text_layer, "Select");
   DictionaryIterator *iter;

    if (app_message_outbox_begin(&iter) != APP_MSG_OK) {
        return;
    }
    if (dict_write_uint8(iter, 4, 5) != DICT_OK) {
        return;
    }
    app_message_outbox_send();
}

static void set_timer() {
  if (running){
       timer = app_timer_register(timer_frequency, timer_callback, NULL);
    }
}

static void up_click_handler(ClickRecognizerRef recognizer, void *context) {
  text_layer_set_text(text_layer, "Up");
}

static void down_click_handler(ClickRecognizerRef recognizer, void *context) {
  text_layer_set_text(text_layer, "Down");
}

static void click_config_provider(void *context) {
  window_single_click_subscribe(BUTTON_ID_SELECT, select_click_handler);
  window_single_click_subscribe(BUTTON_ID_UP, up_click_handler);
  window_single_click_subscribe(BUTTON_ID_DOWN, down_click_handler);
}

static void window_load(Window *window) {
  Layer *window_layer = window_get_root_layer(window);
  GRect bounds = layer_get_bounds(window_layer);

  text_layer = text_layer_create((GRect) { .origin = { 0, 72 }, .size = { bounds.size.w, 20 } });
  text_layer_set_text(text_layer, "Press a button");
  text_layer_set_text_alignment(text_layer, GTextAlignmentCenter);
  layer_add_child(window_layer, text_layer_get_layer(text_layer));
}

static void window_unload(Window *window) {
  text_layer_destroy(text_layer);
}

static void in_received_handler(DictionaryIterator *iter, void *context){
}

static void timer_callback() {
  if (!running) return;

  AccelData accel = {0, 0, 0};

  accel_service_peek(&accel);
  
  history[last_x].x = accel.x;
  history[last_x].y = accel.y;
  history[last_x].z = accel.z;

  if (last_x >= sample_window){
    get_variance();
  }

  APP_LOG(APP_LOG_LEVEL_DEBUG, "%i, %i, %i", accel.x, accel.y, accel.z); 
  last_x++;
  if (last_x >= HISTORY_MAX) last_x = 0;
  //layer_mark_dirty(graph_layer);

  set_timer();
}


static void get_variance(){
  int16_t recent_max = 0;
  int16_t recent_min = 0;
  
  //find recent high and low
  for (int k = last_x - sample_window; k <= last_x; k++){
    if (history[k].y >= recent_max) recent_max = history[k].y;
    else if (history[k].y <= recent_min) recent_min = history[k].y;
  }
  
  //measure variance
  int16_t difference = recent_max - recent_min;
  if (difference > 2000){
    DictionaryIterator *iter;

    if (app_message_outbox_begin(&iter) != APP_MSG_OK) {
        return;
    }
    if (dict_write_uint8(iter, 4, 5) != DICT_OK) {
        return;
    }
    app_message_outbox_send();
    //throw a handshake
    APP_LOG(APP_LOG_LEVEL_DEBUG, "Handshake: %i", difference);
  }
}

static void app_message_init(void){
    app_comm_set_sniff_interval(SNIFF_INTERVAL_REDUCED);
    app_message_open(64, 16);
    app_message_register_inbox_received(in_received_handler);
}

static void handle_accel(AccelData *accel_data, uint32_t num_samples) {
  // nothing
}

static void start_accel(){
  running = !running;
    if (running) {
    // should process events - 5 times a second
      accel_service_set_sampling_rate(ACCEL_SAMPLING_10HZ);
      accel_data_service_subscribe(0, handle_accel);
      layer_set_hidden((Layer *)text_layer, true);
      //layer_set_hidden((Layer *)speed_up_layer, true);
      //layer_set_hidden((Layer *)speed_down_layer, true);
    }
    else {
      layer_set_hidden((Layer *)text_layer, false);
      //layer_set_hidden((Layer *)speed_up_layer, false);
      //layer_set_hidden((Layer *)speed_down_layer, false);
      accel_data_service_unsubscribe();
    }

    set_timer();
}

static void init(void) {
  window = window_create();
  window_set_click_config_provider(window, click_config_provider);
  window_set_window_handlers(window, (WindowHandlers) {
    .load = window_load,
    .unload = window_unload,
  });
  const bool animated = true;
  window_stack_push(window, animated); 
  app_message_init();

  //plug dummy values into history array, start timer
  for (int i=0; i<HISTORY_MAX; ++i) {
    AccelData pt = {0, 0, 0};
    history[i] = pt;
  }

    DictionaryIterator *iter;

      if (app_message_outbox_begin(&iter) != APP_MSG_OK) {
          return;
    }
      if (dict_write_uint8(iter, 4, 5) != DICT_OK) {
                      return;
    }
      app_message_outbox_send();

    start_accel();
//  set_timer();
}

static void deinit(void) {
  accel_data_service_unsubscribe();
  window_destroy(window);
}

int main(void) {
  init();

  APP_LOG(APP_LOG_LEVEL_DEBUG, "Done initializing, pushed window: %p", window);

  app_event_loop();
  deinit();
}
