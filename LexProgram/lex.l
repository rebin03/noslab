%{
int count = 0;
%}

identifier [a-zA-Z][a-zA-Z0-9]*
invalidtokens [0-9]+[a-zA-Z]+

%%

#.* { printf("\n%s is a preprocessor directive", yytext); }
int|float|char|double|while|for|do|if|break|continue|void|switch|case|long|struct|const|typedef|return|else|goto|union { printf("\n%s is a keyword", yytext); }
[;,()\{\}] { if (!count) printf("\n%s is a separator", yytext); }
{identifier} { if (!count) printf("\n%s is an identifier", yytext); }
{invalidtokens} { if (!count) printf("\n%s is an invalid tokens", yytext); }
\"{identifier}[^\"]*\" { if (!count) printf("\n%s is a string", yytext); }
\"[^\"]*\" { if (!count) printf("\n%s is a argument", yytext); }
[0-9]+ { if (!count) printf("\n%s is a Number", yytext); }
\|\||\&\&|\+|\-|\*|\/|\%|\=|\=\=|\!\=|\<|\<=|\>|>= { if (!count) printf("\n%s is an operator", yytext); }

[ \t\n] ;

%%

int main() {
    FILE *file;
    file = fopen("in.c", "r");
    if (!file) {
        printf("unable to open file");
        exit(0);
    }
    yyin = file;
    yylex();
    return 0;
}

int yywrap() {
    fclose(yyin);
    return 1; 
}