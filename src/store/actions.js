import {
  fetchAdminDelete,
  fetchAdminUpdate,
  fetchData,
  fetchLogin,
  fetchLogout,
  fetchRegister, fetchSearch,
  fetchBoard,
  uploadPostData, getPostData, deletePostData,
  uploadCommentData, getCommentData, deleteCommentData, updatePostData,
} from '@/api';

export default {
  async FETCH_LOGIN(context, { account, rememberMe }) {
    return await fetchLogin(account, rememberMe);
  },
  async FETCH_LOGOUT() {
    return await fetchLogout();
  },
  async FETCH_REGISTER(context, account) {
    return await fetchRegister(account);
  },
  async FETCH_DATA({ commit }, url) {
    try {
      const response = await fetchData(url);

      // 세션이 만료되었는지 확인
      if (hasSessionExpired(response.data)) {
        commit('SESSION_EXPIRED');
        return;
      }

      commit('SET_HTTP_STATUS', response.status);

      if (url === '/home') {
        commit('SET_USER_INFO', response.data);
      } else if (url === '/admin') {
        commit('SET_ADMIN_DATA', response.data);
      }
    } catch (error) {
      commit('SET_HTTP_STATUS', error.status)
      console.log(error);
    }
  },
  async FETCH_ADMIN_UPDATE(context, data) {
    return await fetchAdminUpdate(data);
  },
  async FETCH_ADMIN_DELETE(context, data) {
    return await fetchAdminDelete(data);
  },
  async FETCH_SEARCH({ commit }, search) {
    try {
      const response = await fetchSearch(search);
      commit('SET_ADMIN_DATA', response.data);
    } catch (error) {
      console.log(error);
    }
  },
  async FETCH_SEARCH_SCROLL({ commit }, { search, page }) {
    try {
      const response = await fetchSearch(search, page);
      commit('ADD_ADMIN_DATA', response.data);
      return response;
    } catch (error) {
      console.log(error);
    }
  },
  async FETCH_BOARD({ commit }, { title, writer, page }) {
    try {
      const response = await fetchBoard(title, writer, page);
      commit('SET_BOARD', response.data);
      return response;
    } catch(error) {
      console.log(error);
      throw Error("fetch board data error");
    }
  },
  async UPLOAD_POST_DATA(context, formData) {
    try {
      return await uploadPostData(formData);
    } catch (error) {
      console.log(error);
      throw new Error("post upload error");
    }
  },
  async GET_POST_DATA({ commit }, postId) {
    const response = await getPostData(postId);
    commit('SET_POST_DTO', response.data);
  },
  async UPDATE_POST_DATA(context, formData) {
    try {
      return await updatePostData(formData);
    } catch (error) {
      console.log(error);
      throw new Error("post update error");
    }
  },
  async DELETE_POST_DATA(content, postDto) {
    return await deletePostData(postDto);
  },
  async INIT_POST_DATA({ commit }) {
    commit('INIT_POST_DTO');
  },
  async UPLOAD_COMMENT_DATA(content, commentData) {
    try {
      return await uploadCommentData(commentData);
    } catch (error) {
      console.log(error);
    }
  },
  async GET_COMMENT_DATA({ commit }, { postId, page }) {
    try {
      const response = await getCommentData(postId, page);
      if (response.data.first) {
        commit('SET_COMMENT_DTO', response.data)
      } else {
        commit('UPDATE_COMMENT_DTO', response.data);
      }
    } catch (error) {
      console.log(error);
    }
  },
  async DELETE_COMMENT_DATA(content, commentDto) {
    return await deleteCommentData(commentDto);
  }
}

function hasSessionExpired(data) {
  // 세션이 만료될 경우 서버에서 다음과 같은 문자열 값이 전달된다.
  return data.toString().includes('This session has been expired');
}