package se.knowit.bookitregistration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookitRegistrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookitRegistrationApplication.class, args);
    }

    @Bean
    CommandLineRunner init() {
        return args -> {

            //Obligatorisk ASCII-art
            System.out.println(
                    " (                                            )              \n" +
                            " )\\ )            *   )                     ( /( (         )  \n" +
                            "(()/( (        ` )  /(   (     )     )     )\\()))\\ )   ( /(  \n" +
                            " /(_)))(    (   ( )(_)) ))\\ ( /(    (    |((_)\\(()/(   )\\()) \n" +
                            "(_)) (()\\   )\\ (_(_()) /((_))(_))   )\\  '|_ ((_)/(_)) ((_)\\  \n" +
                            "| _ \\ ((_) ((_)|_   _|(_)) ((_)_  _((_)) | |/ /(_) /  /  (_) \n" +
                            "|  _/| '_|/ _ \\  | |  / -_)/ _` || '  \\()  ' <  / _ \\| () |  \n" +
                            "|_|  |_|  \\___/  |_|  \\___|\\__,_||_|_|_|  _|\\_\\ \\___/ \\__/   \n" +
                            "                                                             \n" +
                            "\n");
        };

    }
}
