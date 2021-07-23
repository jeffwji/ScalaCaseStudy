package com.examples.opt

sealed trait Option[+A] {
  def get():A
  // def flatMap[B](f: A => Option[B]):Option[B] = f(this.get())  // 可以用隐式来实现
}

final case class Some[A](a:A) extends Option[A] {
  override def get(): A = a
}

case object None extends Option[Nothing] {
  override def get(): Nothing = throw new NoSuchElementException
}

object Option {
  def apply[A](a:A):Option[A] = a match{
    case null => None
    case _ => Some(a)
  }

  implicit class OptionImplicits[A](t: Option[A]) {
    def flatMap[B](f: A => Option[B]):Option[B] = f(t.get())
  }

  /*
  不正确的实现。
  implicit class OptionImplicits[A, +T <: Option[A]](t: T) {
      def flatMap[B](f: A => Option[B]):Option[B] = f(t.get())
  }
  */
}

object Test extends App {
  val opt = Option(1)
  val  i = opt.flatMap(v => Option((v*2).toString))
  print(i)
}
