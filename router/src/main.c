#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pcap.h>
#include <prism.h>

#include "rssi.h"

void handler(prism_value rssi)
{
	printf("\033[31mi wipos.%s.%s@%d rssi=%d\033[0m\n", __FILE__, __FUNCTION__, __LINE__, rssi.data);
}


int main()
{
	rssi(&handler);
	return EXIT_SUCCESS;
}
