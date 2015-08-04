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
    
    if ( argc < 2 )
    {
        printf("usage : quiz_xml_generator <xmlfilename>\n");
        goto cleanup;
    }
    printf("<question> \n"
    "<catagory></catagory>\n"
    "<no></no>\n"
    "<q></q>\n"
    "<optiona></optiona>\n"
    "<optionb></optionb>\n"
    "<optionc></optionc>\n"
    "<optiond></optiond>\n"
    "<ans></ans>\n"
    "<info></info>\n"
    "<data></data>\n"
    "</question>\n");

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
                   "<questions>\n\n");
    }
    else
    {
        //copy the already present content of xml file to a buffer
        rewind(fp);
        if (fread(buffer, sizeof(char), filesize, fp) <= 0)
            printf("ERROR in reading the file\n");
        bufSize = filesize - 12;
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
    nNum = 1;

    for (i = 0 ; i < nNum; i++)
    {
        getLine("Enter Question >", ques, 4096);

        getLine("Enter option 1 >", optiona, 4096);

        getLine("Enter option 2 >", optionb, 4096);

        getLine("Enter option 3 >", optionc, 4096);

        getLine("Enter option 4 >", optiond, 4096);

        printf("Correct option > ");
        scanf("%d",&ans);

        printf("Addition info ? enter 0 for no 1 for yes > ");
        scanf("%d",&info);
	c = getchar();
        if (info == 1)
            getLine("Enter Additiona information >", data, 16384);
        else printf("skip additional info as info flag is not set\n");


        tagLen += sprintf(tag + tagLen,
                 "<question>\n<catagory>%d</catagory>\n<no>%d</no>\n",
                 catagory,qNo);
        tagLen += sprintf(tag + tagLen,
                 "<q>%s</q>\n",
                  ques);
        tagLen += sprintf(tag + tagLen,
                 "<optiona>%s</optiona>\n",
                  optiona);
        tagLen += sprintf(tag + tagLen,
                 "<optionb>%s</optionb>\n",
                  optionb);
        tagLen += sprintf(tag + tagLen,
                 "<optionc>%s</optionc>\n",
                  optionc);
        tagLen += sprintf(tag + tagLen,
                 "<optiond>%s</optiond>\n",
                  optiond);
        tagLen += sprintf(tag + tagLen,
                 "<ans>%d</ans>\n",
                  ans);
        tagLen += sprintf(tag + tagLen,
                 "<info>%d</info>\n",
                  info);
        if(info !=0)
            tagLen += sprintf(tag + tagLen,
                  "<data>%s</data>\n",
                  data);

        tagLen += sprintf(tag + tagLen,
                  "</question>\n");

    }
    bufSize += sprintf(buffer + bufSize,
               "%s\n\n</questions>",tag);

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
