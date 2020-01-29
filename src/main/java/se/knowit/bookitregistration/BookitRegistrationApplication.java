package se.knowit.bookitregistration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import se.knowit.bookitregistration.model.Participant;
import se.knowit.bookitregistration.model.Registration;
import se.knowit.bookitregistration.service.RegistrationService;

import java.util.UUID;

@SpringBootApplication
public class BookitRegistrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookitRegistrationApplication.class, args);
    }

    @Bean
    CommandLineRunner init(RegistrationService service) {
        return args -> {
            String profile = env.getProperty("spring.profiles.active");
            if (!profile.equalsIgnoreCase("prod")) {
                Registration registration = new Registration();
                registration.setEventId(UUID.randomUUID());
                Participant participant = new Participant();
                participant.setEmail("ulf.lundell@knowit.se");
                registration.setParticipant(participant);
                service.save(registration);

                Registration registration2 = new Registration();
                registration2.setEventId(UUID.randomUUID());
                participant = new Participant();
                participant.setEmail("lars.bandage@knowit.se");
                registration2.setParticipant(participant);
                service.save(registration2);
            }
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
