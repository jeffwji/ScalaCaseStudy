import cats.{Monad, ~>}
import org.scalatest.FlatSpec
import cats.implicits._
import cats._
import cats.data.{State, StateT, Writer, WriterT}

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

            implicit val _semigroup: Semigroup[Email] = (x: Email, _: Email) => x

            implicit object notifyInterpreterMonad extends Monad[EMailWriter] {
                override def pure[A](a: A): EMailWriter[A] = Writer(Email("default","",""), a)
                override def flatMap[A, B](fa: EMailWriter[A])(f: A => EMailWriter[B]): EMailWriter[B] = fa flatMap f
                override def tailRecM[A, B](a: A)(f: A => EMailWriter[Either[A, B]]): EMailWriter[B] = f(a)map{
                    case Right(b) => b
                }
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
        import EmailDsl._

        emailNotify[EMailWriter].run
    }
}
