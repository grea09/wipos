#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pcap.h>
#include <prism.h>

#include "rssi.h"
#include "RSSI_list.h"

void* rssi_thread (void* args)
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
	
	l = NULL;
  l = malloc (sizeof(DeviceList*));
  (*l) = NULL;
	
	while ( 1 ) 
	{
		packet = pcap_next(handle, &header);
		if ( ((unsigned int *) packet)[0] == 0x41 ) 
		{
			ph = (prism_header *) packet;
			eh = (ieee80211_header *) (packet + ph->msglen);
			
			// Check if FromDS flag equals 0
			if ( (eh->frame_control & 0xc0) == 0x80 )
			{
  			DeviceList* tmpDevice = is_known (l, eh->source_addr);
				if (tmpDevice == NULL)
				{
					tmpDevice = add_device (l, eh->source_addr);
				}
				add_rssi_sample(tmpDevice, ph->rssi.data);
				delete_outdate(l);
				print (l);
				printf("-------------------------------------------\n");
			}
		}
	}
	
	pcap_close ( handle );
}
