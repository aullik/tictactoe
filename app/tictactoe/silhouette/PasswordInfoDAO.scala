package tictactoe.silhouette

import com.google.inject.{Inject, Singleton}
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO
import tictactoe.controllers.webControllers.UserController
import grizzled.slf4j.Logging
import tictactoe.exceptions.PersistenceException.EntityNotFoundException
import tictactoe.silhouette.Implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/** DAO for PasswordInfo (email + password)
  *
  */
@Singleton
class PasswordInfoDAO @Inject()(
                                 val userController: UserController
                               ) extends DelegableAuthInfoDAO[PasswordInfo] with Logging {

  override def find(loginInfo: LoginInfo): Future[Option[PasswordInfo]] =
    try {
      info(s"find(${loginInfo.providerKey})")
      val player = userController.getUserByEmail(loginInfo2key(loginInfo)).get
      if (player.emailConfirmed) {
        Future.successful(Option(pwd2passwordInfo(userController.getPassword(player.id))))
      } else {
        Future.successful(None)
      }
    } catch {
      case e: EntityNotFoundException => Future.successful(None)
    }

  override def update(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    try {
      info(s"update(${loginInfo.providerKey})")
      val user = userController.getUserByEmail(loginInfo2key(loginInfo)).get
      userController.modifyPasswordHash(user.id, passwordInfo2pwd(authInfo))
      Future.successful(authInfo)
    } catch {
      case e: EntityNotFoundException => throw new Exception("PasswordInfoDAO - update : the user must exists to update its password")
    }
  }

  override def remove(loginInfo: LoginInfo): Future[Unit] = {
    info(s"remove(${loginInfo.providerKey})")
    throw new Exception("Check later")
  }

  override def save(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    info(s"save(${loginInfo.providerKey})")
    find(loginInfo).flatMap {
      case Some(_) => update(loginInfo, authInfo)
      case None => add(loginInfo, authInfo)
    }
  }

  override def add(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    info(s"add(${loginInfo.providerKey})")
    update(loginInfo, authInfo)
  }
}
