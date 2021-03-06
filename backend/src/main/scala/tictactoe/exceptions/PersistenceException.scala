package tictactoe.exceptions

sealed abstract class PersistenceException(name: String) extends RuntimeException(name) {
}

object PersistenceException {

  case class IllegalEmailException() extends PersistenceException("IllegalEmail")

  case class IllegalNameException() extends PersistenceException("IllegalName")

  case class IllegalPasswordException() extends PersistenceException("IllegalPassword")

  case class EmailInUseException() extends PersistenceException("EmailInUse")

  case class NameInUseException() extends PersistenceException("NameInUse")

  case class PasswordAlreadySetException() extends PersistenceException("PasswordAlreadySet")

  case class EntityNotFoundException() extends PersistenceException("EntityNotFound")

  case class DuplicateKeyException() extends PersistenceException("DuplicateKey")

  case class PasswordNotSetException() extends PersistenceException("PasswordNotSet")

  case class PasswordsDoNotMatchException() extends PersistenceException("PasswordsDoNotMatch")

  case class WrongPasswordException() extends PersistenceException("WrongPassword")

  case class InvalidAuthenticationException() extends PersistenceException("InvalidAuthentication")

  case class NoAuthorizationException() extends PersistenceException("NoAuthorization")


}
