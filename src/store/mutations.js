import { BoardDto } from '@/dto/BoardDto';

export default {
  SET_USER_INFO(state, user) {
    state.userInfo = user;
  },
  SET_HTTP_STATUS(state, status) {
    state.httpStatus = status;
  },
  SESSION_EXPIRED(state) {
    state.httpStatus = 401;
  },
  SET_ADMIN_DATA(state, data) {
    state.admin.data = {
      users: data.content,
      totalSize: data.totalElements,
      lastPage: data.last,
    };
  },
  ADD_ADMIN_DATA(state, data) {
    const adminData = state.admin.data;
    adminData.users.push(...data.content);

    // 중복 제거
    let set = new Set(adminData.users.map(JSON.stringify));
    adminData.users = Array.from(set).map(JSON.parse);

    adminData.totalSize = data.totalElements;
    adminData.lastPage = data.last;
  },
  SET_BOARD(state, data) {
    state.boardDto = new BoardDto(data);
  }
}