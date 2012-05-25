#include "RSSI_list.h"

struct timezone tz = {0, 0};

DeviceList* is_known(DeviceList ** l , char mac_addr [ 6 ])
{
  if (l == NULL)
		return NULL;
	DeviceList* tmp_l = *l;
	int known = 0;
	while (tmp_l != NULL && !known)
	{
	  printf("plop");
		if (strncmp(tmp_l->dl_mac_address, mac_addr, 6) == 0)
			known = 1;
		tmp_l = tmp_l->dl_next;
	}
	if (known)
		return tmp_l;
	else
	{
	  printf("Unknown\n");
		return NULL;
	}
}

DeviceList* add_device (DeviceList ** l , char mac_addr [ 6 ])
{
  if (l == NULL) // liste vide
  {
	  printf("Void list\n");
    l = malloc (sizeof(DeviceList*));
    (*l) = NULL;
  }
	DeviceList* tmp_l = *l;
	while (tmp_l != NULL)
	{
	  printf("plip");
		tmp_l = tmp_l->dl_next;
	}
	tmp_l = malloc (sizeof(DeviceList));
	tmp_l->dl_next = NULL;
	tmp_l->dl_rssi_list = NULL;
	strncpy(tmp_l->dl_mac_address, mac_addr, 6);
	printf("added device\n");
	return tmp_l;
}

void clear_device_list (DeviceList ** l)
{
	DeviceList* tmp_l = *l;
	DeviceList* tmp_l_save;
	while (tmp_l != NULL)
	{
		clear_rssi_list (tmp_l);
		tmp_l_save = tmp_l->dl_next;
		free (tmp_l);
		tmp_l = tmp_l_save;
	}
}

void add_rssi_sample (DeviceList * l, int rssi_value)
{
	RssiList* tmp_rssi = l->dl_rssi_list;
	while (tmp_rssi != NULL)
	{
		tmp_rssi = tmp_rssi->rl_next;
	}
	tmp_rssi = malloc (sizeof(RssiList));
	gettimeofday(&(tmp_rssi->rl_date), &tz);
	tmp_rssi->rl_rssi_value = rssi_value;
	tmp_rssi->rl_next = NULL;
}

void clear_rssi_list (DeviceList * l)
{
	RssiList* tmp_rssi;
	RssiList* tmp_rssi_save;
	
	tmp_rssi = l->dl_rssi_list;
	while (tmp_rssi != NULL) 
	{
		tmp_rssi_save = tmp_rssi->rl_next;
		free (tmp_rssi);
		tmp_rssi = tmp_rssi_save;
	}
}

void delete_outdate (DeviceList ** l)
{
  if (l == NULL)
    return;
	struct timeval tv;
  gettimeofday(&tv, &tz);
	DeviceList* tmp_l = *l;
	while (tmp_l != NULL)
	{
	  delete_outdated (tmp_l, tv);
	  tmp_l = tmp_l->dl_next;
	}
}

void delete_outdated (DeviceList * l, struct timeval current_time)
{
	RssiList* tmp_rssi;
	RssiList* tmp_rssi_save;
	
	tmp_rssi = l->dl_rssi_list;
	tmp_rssi_save = l->dl_rssi_list;
	while (tmp_rssi != NULL && tmp_rssi->rl_next != NULL) 
	{
		if ( /* TODO time condition over */ 0) 
		{
			tmp_rssi_save = tmp_rssi->rl_next;
			tmp_rssi->rl_next = tmp_rssi->rl_next->rl_next;
			free (tmp_rssi_save);
		}
		else
		{
			tmp_rssi = tmp_rssi->rl_next;
		}
	}
}

void print (DeviceList** l)
{
  if (l == NULL)
    return;
  DeviceList* tmp_l = *l;
  while (tmp_l != NULL)
  {
    printf("| MAC %02x:%02x:%02x:%02x:%02x:%02x |\t", 
        tmp_l->dl_mac_address[0],tmp_l->dl_mac_address[1],tmp_l->dl_mac_address[2],
        tmp_l->dl_mac_address[3],tmp_l->dl_mac_address[4],tmp_l->dl_mac_address[5]); 
    RssiList* tmp_rssi = tmp_l->dl_rssi_list;
    while (tmp_rssi != NULL) 
    {
      printf("%d\t", tmp_rssi->rl_rssi_value);
    }
    printf("\n");
    fflush(stdout);
  }
}







