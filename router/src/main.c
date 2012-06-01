#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pcap.h>
#include <prism.h>
#include <pthread.h>

#include "rssi.h"
#include "socket.h"

void handler(prism_value rssi)
{
	printf("\033[31mi wipos.%s.%s@%d rssi=%d\033[0m\n", __FILE__, __FUNCTION__, __LINE__, rssi.data);
}


int main()
{
	// rssi(&handler);
	pthread_t rssiT;
	pthread_t socketT;
	
	pthread_create (&rssiT, NULL, rssi_thread, (void*)0);
	pthread_create (&socketT, NULL, socket_thread, (void*)0);
	
	pthread_join (rssiT, NULL);
  pthread_join (socketT, NULL);
	
	return EXIT_SUCCESS;
}
