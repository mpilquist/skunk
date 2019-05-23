// Copyright (c) 2018 by Rob Norris
// This software is licensed under the MIT License (MIT).
// For more information see LICENSE or https://opensource.org/licenses/MIT

package skunk

import cats.Contravariant
import skunk.util.Origin

/**
 * SQL and parameter encoder for a statement that returns no rows. We assume that `sql` has the
 * same number of placeholders of the form `$1`, `$2`, etc., as the number of slots encoded by
 * `encoder`, and that the parameter types specified by `encoder` are consistent with the schema.
 * The `check` methods on [[skunk.Session Session]] provide a means to verify this assumption.
 *
 * You can construct a `Command` directly, although it is more typical to use the `sql`
 * interpolator.
 *
 * {{{
 * sql"INSERT INTO foo VALUES ($int2, $varchar)".command // Command[Short ~ String]
 * }}}
 *
 * @param sql A SQL statement returning no rows.
 * @param encoder An encoder for all parameters `$1`, `$2`, etc., in `sql`.
 *
 * @see [[skunk.syntax.StringContextOps StringContextOps]] for information on the `sql`
 *   interpolator.
 * @see [[skunk.Session Session]] for information on executing a `Command`.
 *
 * @group Statements
 */
final case class Command[A](
  sql:     String,
  origin:  Origin,
  encoder: Encoder[A]
) extends Statement[A] {

  /**
   * Command is a [[https://typelevel.org/cats/typeclasses/contravariant.html contravariant
   * functor]].
   * @group Transformations
   */
  def contramap[B](f: B => A): Command[B] =
    Command(sql, origin, encoder.contramap(f))

}

/** @group Companions */
object Command {

  /**
   * Command is a [[https://typelevel.org/cats/typeclasses/contravariant.html contravariant
   * functor]].
   * @group Typeclass Instances
   */
  implicit val CommandContravariant: Contravariant[Command] =
    new Contravariant[Command] {
      def contramap[A, B](fa: Command[A])(f: B => A) =
        fa.contramap(f)
    }

}