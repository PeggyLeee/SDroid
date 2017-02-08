/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gcm;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.GcmUsers;
import dao.GcmUsersDao;

/**
 * Servlet that registers a device, whose registration id is identified by
 * {@link #PARAMETER_REG_ID}.
 *
 * <p>
 * The client app should call this servlet everytime it receives a
 * {@code com.google.android.c2dm.intent.REGISTRATION C2DM} intent without an
 * error or {@code unregistered} extra.
 */
@SuppressWarnings("serial")
@WebServlet(name = "RegisterServlet", urlPatterns = { "/RegisterServlet.do" })
public class RegisterServlet extends BaseServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException {

		GcmUsersDao guDao = new GcmUsersDao();
		
		String name = getParameter(req, "name");
		String email = getParameter(req, "email");
		String regId = getParameter(req, "regId");
		
		GcmUsers user = guDao.getByEmail(email);
		if(user != null){
			user.setGcmId(regId);
			user.setStatus(0);
			guDao.updateByVo(user);
		}else{
			GcmUsers newUser = new GcmUsers();
			newUser.setGcmId(regId);
			newUser.setName(name);
			newUser.setEmail(email);
			newUser.setStatus(0);
			guDao.insert(newUser);
		}
		
		setSuccess(resp);
	}

}
