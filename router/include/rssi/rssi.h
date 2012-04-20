#ifndef _RSSI_H
#define _RSSI_H


typedef struct
{
  u_short frame_control;
  u_short frame_duration;
  u_char recipient[6];
  u_char source_addr[6];
  u_char address3[6];
  u_short sequence_control;
  u_char address4[6];
} ieee80211_header;

void rssi(void (*handler)(prism_value rssi));

#endif
