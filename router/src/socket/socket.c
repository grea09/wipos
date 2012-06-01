#include "socket.h"

#define PORT 1337
#define MY_IP " 192.168.1.1 "
#define BUFFER_SIZE 5000

void* socket_thread (void* args)
{
	int skfd ;
	struct sockaddr_in addr , remote_addr;
	char buffer[BUFFER_SIZE];
	size_t addr_size ;

	/* Content omitted */
	
	addr.sin_family = AF_INET;
	addr.sin_port = htons (PORT);
	inet_pton (AF_INET, MY_IP, &addr.sin_addr );
	memset(&addr.sin_zero , '\0' , 8 );
	skfd = socket (PF_INET, SOCK_DGRAM, 0 );

	if ( skfd == -1 ) {
		printf ( " Could not open skfd\n" );
		pthread_exit (NULL);
	}
	if ( bind ( skfd, ( struct sockaddr * ) &addr,
		sizeof ( struct sockaddr ) ) == -1 ) {
		printf ( "Error when binding socket \n" );
		pthread_exit (NULL);
	}
	
	/* Content omitted */

	while (1)	{
		if ( recvfrom ( skfd, buffer, BUFFER_SIZE - 1, 0, ( struct sockaddr * ) &remote_addr,	&addr_size ) > 0 ) {
			/* Use data from buffer
			( now remote addr contains addr info on remote device ) */
			request_t request = parse (buffer);
			if (request.op == "") {
			  // error
			  printf("--- ERROR : parse (buffer) \n");
			}
			request.average = average (l, request.mac_addr);
			char* msg = display(request);
			sendto (skfd, msg, sizeof(msg), 0, &remote_addr,	&addr_size);
			free (msg);
		}
	}
	
	close(skfd);
	
}

request_t parse (char* buffer) {
  int semi_colon_nb = 0;
  char* tmp = buffer;
  while (*tmp) {
    semi_colon_nb += (*tmp == ';');
    tmp++;
  }
  request_t value = {.op = "", .semi_colon_nb = semi_colon_nb};
  if (semi_colon_nb > 1) {
    sscanf (buffer, "%s;%lf;%lf;%d;%02X:%02X:%02X:%02X:%02X:%02X",
        value.op, &value.x, &value.y, &value.mapID, 
        &value.mac_addr[0], &value.mac_addr[1], &value.mac_addr[2], 
        &value.mac_addr[3], &value.mac_addr[4], &value.mac_addr[5]);
  }
  else if (semi_colon_nb == 1) {
    sscanf (buffer, "%s;%02X:%02X:%02X:%02X:%02X:%02X",
        value.op,
        &value.mac_addr[0], &value.mac_addr[1], &value.mac_addr[2], 
        &value.mac_addr[3], &value.mac_addr[4], &value.mac_addr[5]);
  }
  int s;
	struct ifreq tmpIfReq;
	s = socket(PF_INET, SOCK_DGRAM, 0);
	memset(&tmpIfReq, 0x00, sizeof(tmpIfReq));
	strcpy(tmpIfReq.ifr_name, "wl0");
	ioctl(s, SIOCGIFHWADDR, &tmpIfReq);
  memcpy(value.my_mac_addr, tmpIfReq.ifr_hwaddr.sa_data, 6*sizeof(unsigned char));
	close(s);
  return value;
}


char* display (request_t value) {
  char** strp = NULL;
  if (value.semi_colon_nb > 1) {
    asprintf (strp, "%s;%lf;%lf;%d;%02X:%02X:%02X:%02X:%02X:%02X",
          value.op, value.x, value.y, value.mapID, 
          value.mac_addr[0], value.mac_addr[1], value.mac_addr[2], 
          value.mac_addr[3], value.mac_addr[4], value.mac_addr[5]);
  }
  else if (value.semi_colon_nb == 1) {
    asprintf (strp, "%s;%02X:%02X:%02X:%02X:%02X:%02X",
        value.op,
        value.mac_addr[0], value.mac_addr[1], value.mac_addr[2], 
        value.mac_addr[3], value.mac_addr[4], value.mac_addr[5]);
  }
  else {
    return NULL;
  }
  return *strp;
}












