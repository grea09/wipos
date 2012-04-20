#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pcap.h>
#include <prism.h>

#include "rssi.h"




void rssi(void (*handler)(prism_value rssi))
{
	char errbuf[PCAP_ERRBUF_SIZE];
	pcap_t * handle = NULL;
	
	prism_header *ph;
	ieee80211_header * eh;
	
	struct pcap_pkthdr header;
	const u_char * packet;
	
	handle = pcap_open_live("prism0", BUFSIZ, 1, 1000, errbuf);
	
	if ( handle == NULL ) 
	{
		printf("Could not open pcap on interface prism0 : %s\n", errbuf);
		exit(EXIT_FAILURE);
	}
	
	while ( 1 ) 
	{
		packet = pcap_next(handle, &header);
		if ( ((unsigned int *) packet)[0] == 0x41 ) 
		{
			ph = (prism_header *) packet;
			eh = (ieee80211_header *) (packet + ph->msglen);
			
			// Check if FromDS flag equals 0
			if ( (eh->frame_control & 0xc0) == 0x80 ) //TODO Check ToDS = 1
			{ 
				/* Do something with (ph->rssi).data */
				(*handler)(ph->rssi);
				//TODO get MAC address
			}
		}
	}
	
	pcap_close ( handle );
}
