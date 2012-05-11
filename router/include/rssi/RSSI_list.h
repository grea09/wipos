/* Header RSSI_list */

#ifndef __RSSI_LIST_h
#define __RSSI_LIST_h

typedef struct RssiList {
	struct timeval rl_date ; // Expiration date
	int rl_rssi_value ;
	struct RssiList * rl_next ;
} RssiList ;

typedef struct DeviceList {
	char dl_mac_address [ 6 ] ;
	RssiList * dl_rssi_list ;
	struct DeviceList * dl_next ;
} DeviceList ;

void add_device (DeviceList ** l , char mac_addr [ 6 ]);
void clear_device_list (DeviceList ** l);
void add_rssi_sample (DeviceList * l, int rssi_value);
void clear_rssi_list (DeviceList * l);
void delete_outdated (DeviceList * l, struct timeval current_time);
DeviceList* is_known(DeviceList ** l , char mac_addr [ 6 ]);


#endif /* __RSSI_LIST_h */
