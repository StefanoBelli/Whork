#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <string.h>
#include <stdlib.h>




int main(int args, char **argv){
	FILE *file, *fileout;
	char *out, *text, *mix, *without;
	
// 	file = fopen("comuni.sql", "r");
// 	fileout = fopen("town", "w+");
// 	file = fopen("province.sql", "r");
// 	fileout = fopen("province", "w+");
	file = fopen("regioni.sql", "r");
 	fileout = fopen("region", "w+");
// 	text = "INSERT INTO Town(PostalCode, Name, Province_code) ";
// 	text = "INSERT INTO Province(Code, Region) ";
	text = "INSERT INTO Region(Name) ";

	
	while(fscanf(file, "%ms", &out) != EOF){
		if(out[0] == 'V' && out[1] == 'A' && out[2] == 'L' && out[3] == 'U' && out[4] == 'E' && out[5] == 'S'){
			mix = (char *)malloc(1000);
			strcat(mix, text);
			strcat(mix, out);
			if(out[strlen(out)-1] == ';'){
				goto next;
			}
			while(1){
				strcat(mix, " ");
				fscanf(file, "%ms", &out);
				strcat(mix, out);
				if(out[strlen(out)-1] == ';') break;
				free(out);
			}
			next:
				fprintf(fileout, "%s\n", mix);
				free(mix);
		}
		free(out);
	}
	
// 	system("cat town");
// 	system("cat province");
 	system("cat region");
	fclose(file);
	fclose(fileout);
	return 0;
	
}
