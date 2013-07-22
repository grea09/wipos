#include "RSSI_list.h"

struct timezone tz = {0, 0};

DeviceList* is_known(DeviceList ** l , unsigned char  mac_addr [ 6 ])
{

  if (l == NULL)
		return NULL;
	DeviceList* tmp_l = *l;
	int known = 0;
	while (tmp_l != NULL && !known)
	{
		if (memcmp(tmp_l->dl_mac_address, mac_addr, 6*sizeof(char)) == 0)
			known = 1;
		else 
  		tmp_l = tmp_l->dl_next;
	}
	if (known) {
		return tmp_l;
	}
	else
	{
	  printf("Unknown\n");
		return NULL;
	}
}

double average (DeviceList ** l , unsigned char mac_addr [ 6 ])
{
  if (l == NULL) {
    printf("VOID\n");
		return -95;
	}
	DeviceList* tmp_l = is_known (l, mac_addr);
	if (tmp_l == NULL) {
	  // unknown mac address
	  return -95;
	}
	else {
	  double average = 0;
	  double nbData = 0;
	  RssiList* tmp_rssi = tmp_l->dl_rssi_list;
    while (tmp_rssi != NULL) 
    {
      average += tmp_rssi->rl_rssi_value;
      nbData++;
      tmp_rssi = tmp_rssi->rl_next;
    }
	  return (average / nbData);
	}
  
}

DeviceList* add_device (DeviceList ** l , unsigned char mac_addr [ 6 ])
{
    printf("| \033[1m MAC %02X:%02X:%02X:%02X:%02X:%02X \033[0m |\t", 
        mac_addr[0],mac_addr[1],mac_addr[2],
        mac_addr[3],mac_addr[4],mac_addr[5]); 
  if (l == NULL) // liste vide
  {
    l = malloc (sizeof(DeviceList*));
    (*l) = NULL;
  }
	DeviceList* tmp_l = *l;
	if (tmp_l == NULL) // liste vide
  {
	  tmp_l = malloc (sizeof(DeviceList));
	  tmp_l->dl_next = NULL;
	  tmp_l->dl_rssi_list = NULL;
	  memcpy(tmp_l->dl_mac_address, mac_addr, 6*sizeof(char));
	  printf("added first device\n");
	  *l = tmp_l;
	} else {
	  while (tmp_l->dl_next != NULL)
	  {
	    printf("- ");
		  tmp_l = tmp_l->dl_next;
	  }
	  tmp_l->dl_next = malloc (sizeof(DeviceList));
	  tmp_l->dl_next->dl_next = NULL;
	  tmp_l->dl_next->dl_rssi_list = NULL;
	  memcpy(tmp_l->dl_next->dl_mac_address, mac_addr, 6*sizeof(char));
	  printf("added device\n");
	}
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
  if (tmp_rssi == NULL) // liste vide
  {
	  tmp_rssi = malloc (sizeof(DeviceList));
	  tmp_rssi->rl_next = NULL;
	  tmp_rssi->rl_rssi_value = rssi_value;
	  gettimeofday(&(tmp_rssi->rl_date), &tz);
	  l->dl_rssi_list = tmp_rssi;
	} else {
	  while (tmp_rssi->rl_next != NULL)
	  {
		  tmp_rssi = tmp_rssi->rl_next;
	  }
	  tmp_rssi->rl_next = malloc (sizeof(RssiList));
	  gettimeofday(&(tmp_rssi->rl_next->rl_date), &tz);
	  tmp_rssi->rl_next->rl_rssi_value = rssi_value;
	  tmp_rssi->rl_next->rl_next = NULL;
	}
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
	if (l->dl_rssi_list != NULL) {
	  tmp_rssi = l->dl_rssi_list;
	  while (tmp_rssi != NULL && difftime (current_time.tv_sec, tmp_rssi->rl_date.tv_sec) > 1) 
	  {
	    l->dl_rssi_list = tmp_rssi->rl_next;
	    free (tmp_rssi);
	    tmp_rssi = l->dl_rssi_list;
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
    printf("| MAC %02X:%02X:%02X:%02X:%02X:%02X |\t", 
        tmp_l->dl_mac_address[0],tmp_l->dl_mac_address[1],tmp_l->dl_mac_address[2],
        tmp_l->dl_mac_address[3],tmp_l->dl_mac_address[4],tmp_l->dl_mac_address[5]); 
    RssiList* tmp_rssi = tmp_l->dl_rssi_list;
    while (tmp_rssi != NULL) 
    {
      printf("%d\t", tmp_rssi->rl_rssi_value);
      tmp_rssi = tmp_rssi->rl_next;
    }
    tmp_l = tmp_l->dl_next;
    printf("\n");
    fflush(stdout);
  }
}







