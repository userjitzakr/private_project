#include <stdio.h>
#include <string.h>

#define OK       0
#define NO_INPUT 1
#define TOO_LONG 2
static int getLine (char *prmpt, char *buff, size_t sz);

struct 
{
    int catagory;
    int qNo;
    char q[4096];
    char optiona[4096];
    char optionb[4096];
    char optionc[4096];
    char optiond[4096];
    int ans;
    int info;
    char data[16384];
}Questions;

typedef struct Questions Question;

/* xml file writer for quiz */
void main(int argc, char *argv[])
{

    int catagory = 0;
    int qNo = 0;
    char ques[4096] = {0};
    char quote[16384] = {0};
    char optiona[4096] = {0};
    char optionb[4096] = {0};
    char optionc[4096] = {0};
    char optiond[4096] = {0};
    int ans = 0;
    int info = 1;
    int nNum = 0;
    char data[16384] = {0};
    char tag[163840] = {0};
    int tagLen = 0;
    FILE *fp = NULL;
    int i = 0;
    char c;
    long filesize  = 0;
    char buffer[4096000] = {0};
    int bufSize = 0;
    int quoteId = 0;
    int choice = 0;
    
    if ( argc < 3 )
    {
        printf("usage : quote_xml_generator <xmlfilename> <lastquoteid>\n");
        goto cleanup;
    }
 
    quoteId = atoi(argv[2]);
    printf("Last quote id = %d\nis it correct ?\n",quoteId);
    if(quoteId == 0 ) quoteId--;
    scanf("%d",&choice);
    if(choice == 0)
         goto cleanup;
    printf("\n\n");

    fp = fopen(argv[1], "r");
    if (fp != NULL)
        printf("%s is opened succesfully\n",argv[1]);
    else
    {
       printf("Failed to open %s\n",argv[1]);
       goto cleanup;
    }

    /* parse xml file to read the content */

    // get file size
    fseek(fp, 0L, SEEK_END);
    filesize = ftell(fp);
    printf("file size = %ld\n",filesize);
    if(filesize == 0)
    {
        bufSize += sprintf(buffer + bufSize,
                   "<quotes>\n\n");
    }
    else
    {
        //copy the already present content of xml file to a buffer
        rewind(fp);
        if (fread(buffer, sizeof(char), filesize, fp) <= 0)
            printf("ERROR in reading the file\n");
        bufSize = filesize - 10;
    }

    fclose(fp);

    /* open file to add questions */
    fp = fopen(argv[1], "w");

    if (fp != NULL)
        printf("%s is opened succesfully\n",argv[1]);
    else
    {
       printf("Failed to open %s\n",argv[1]);
       goto cleanup;
    }
//    printf("Enter number of questions to be created\n");
    //scanf("%d",&nNum);
    //c = getchar();
    nNum = 10;

printf("Start\npress enter\n");
    c = getchar();
    for (i = 0 ; i < nNum; i++)
    {
        getLine("Ente Quote >", quote, 16384);
        quoteId++;
        tagLen += sprintf(tag + tagLen,
                   "<quote>\n<msg>%s</msg>\n<id>%d</id>\n</quote>\n",
                   quote,quoteId);
    }
    bufSize += sprintf(buffer + bufSize,
               "%s\n\n</quotes>",tag);

    printf("xml file is \n%s\n",buffer);

    fwrite(buffer, sizeof(char), bufSize, fp);
    
    fclose(fp);

cleanup:
    printf("\n\n");
    printf("Exit application\n");
}

static int getLine (char *prmpt, char *buff, size_t sz)
{
    int ch, extra;

    // Get line with buffer overrun protection.
    if (prmpt != NULL) {
        printf ("%s", prmpt);
        fflush (stdout);
    }
    if (fgets (buff, sz, stdin) == NULL)
        return NO_INPUT;

    // If it was too long, there'll be no newline. In that case, we flush
    // to end of line so that excess doesn't affect the next call.
    if (buff[strlen(buff)-1] != '\n') {
        extra = 0;
        while (((ch = getchar()) != '\n') && (ch != EOF))
            extra = 1;
        return (extra == 1) ? TOO_LONG : OK;
    }

    // Otherwise remove newline and give string back to caller.
    buff[strlen(buff)-1] = '\0';
    return OK;
}
