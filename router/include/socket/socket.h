#ifndef _SOCKET_H
#define _SOCKET_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <unistd.h>
#include <net/if.h>
#include <sys/ioctl.h>

#include "RSSI_list.h"

typedef struct {
  char op[5];
  double x, y;
  int mapID;
  int semi_colon_nb;
  unsigned char mac_addr[6];
  unsigned char my_mac_addr[6];
  double average;
} request_t ;

void* socket_thread (void* args);

request_t parse (char* buffer);
char* display (request_t request);

#endif
