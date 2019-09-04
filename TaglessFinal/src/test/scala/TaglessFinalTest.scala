import cats.{Monad, ~>}
import org.scalatest.FlatSpec
import cats.implicits._
import cats.data.{ Writer, WriterT}

class TaglessFinalTest extends FlatSpec{
    "Tagless final" should "" in {
        sealed trait EmailAddress
        final case class Email(to: String, subject: String, body: String)

        trait NotifyDsl[F[_]] {
            def send(to: String, subject: String, body: String): F[Unit]
        }

        object EmailDsl {
            type EMailWriter[A] = Writer[Email, A]
            implicit object notifyInterpreter extends NotifyDsl[EMailWriter]{
                override def send(to: String, subject: String, body: String): EMailWriter[Unit] =
                    Writer(Email(to, subject, body),())
            }
        }

        object Service {
            def emailNotify[F[_]: Monad](implicit notifyDsl: NotifyDsl[F]): F[Unit] = {
                val email = "john@doe.com"
                for {
                    _ <- notifyDsl.send(email, "Hello", "Thank you for registering")
                } yield ()
            }
        }

        import Service._
        import EmailDsl.EMailWriter

        // Fail ???
        emailNotify[EMailWriter].run
    }
}
