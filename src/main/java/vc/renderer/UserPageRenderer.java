package vc.renderer;

import java.util.Set;

import vc.entity.User;
import vc.model.OnlineUsers;

public class UserPageRenderer {
	public static String renderUserPage(User user, boolean isOwner, Long idUserOwner) {
		Set<String> onlineUsers = OnlineUsers.getAllOnlineUsers();
		StringBuilder builder = new StringBuilder();
		builder.append("<html>\n");
		builder.append("<body>\n");
		builder.append("<table>\n");
		builder.append("<tr>\n");
		builder.append("<td style=\"width: 200px;\">\n");
		builder.append("<a href=\"/user?id=" + idUserOwner + "\">Моя страница</a><br>\n");
		builder.append("<a href=\"/news\">Новости</a><br>\n");
		builder.append("<a href=\"/friends\">Друзья</a><br>\n");
		builder.append("<a href=\"/messages\">Сообщения</a><br>\n");
		builder.append("<a href=\"/photo\">Фотки</a><br>\n");
		builder.append("<a href=\"/settings\">Настройки</a><br>\n");
		builder.append("</td>\n");
		builder.append("<td style=\"width: 200px;\">\n");
		builder.append("Аватар\n");
		builder.append("</td>\n");
		builder.append("<td>\n");
		builder.append("<a href=\"/logout\">Выйти</a><br>\n");
		builder.append(user.getName() + " " + user.getSurname() + (isOwner ? " Owner" : "")
				+ (onlineUsers.contains(user.getEmail()) ? " Online" : "") + "<br>\n");
		builder.append("Дата рождения: " + user.getBirthday() + "\n");
		builder.append("</td>\n");
		builder.append("<tr>\n");
		builder.append("<td>\n");
		builder.append("</td>\n");
		builder.append("<td>\n");
		builder.append("Друзья<br>\n");
		builder.append("Друзья онлайн<br>\n");
		builder.append("</td>\n");
		builder.append("<td>\n");
		builder.append("Фотки<br>\n");
		builder.append("Записи<br>\n");
		builder.append("</td>\n");
		builder.append("</tr>\n");
		builder.append("</table>\n");
		builder.append("</body>\n");
		builder.append("</html>");
		return builder.toString();
	}
}
