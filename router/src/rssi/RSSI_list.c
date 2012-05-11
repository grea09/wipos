

DeviceList* is_known(DeviceList ** l , char mac_addr [ 6 ])
{
	DeviceList* tmp_l = *l;
	int known = 0;
	while (tmp_l != NULL && !known)
	{
		if (strncmp(tmp_l->dl_mac_addr, mac_addr, 6) == 0)
			known = 1;
		tmp_l = tmp_l->dl_next;
	}
	if (known)
		return tmp_l;
	else
		return null;
}

void add_device (DeviceList ** l , char mac_addr [ 6 ])
{
	DeviceList* tmp_l = *l;
	while (tmp_l != NULL)
	{
		tmp_l = tmp_l->dl_next;
	}
	tmp_l = malloc (sizeof(DeviceList);
	tmp_l->dl_next = NULL;
	tmp_l->dl_rssi_list = NULL;
	strncpy(tmp_l->dl_mac_address, mac_addr, 6);
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
	tmp_rssi = malloc (sizeof(RssiList);
	tmp_rssi->rl_date = NULL; /* TODO */
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

void delete_outdated (DeviceList * l, struct timeval current_time)
{
	RssiList* tmp_rssi;
	RssiList* tmp_rssi_save;
	
	tmp_rssi = l->dl_rssi_list;
	tmp_rssi_save = l->dl_rssi_list;
	while (tmp_rssi != NULL && tmp_rssi->rl_next != NULL) 
	{
		if ( /* time condition over */ 0) 
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

