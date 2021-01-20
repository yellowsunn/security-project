import { BoardDto } from '@/dto/BoardDto';
import { PostDto } from '@/dto/PostDto';

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
  },
  SET_POST_DTO(state, data) {
    state.postDto = new PostDto(data.title, data.content, data.writer, null, data.postTime, data.hit);
  },
  SET_COMMENT_DTO(state, data) {
    state.commentDto = data;
    state.commentDto.content = state.commentDto.content.filter((comment) => {
      return comment.mainCommentId === null
    });
  },
  UPDATE_COMMENT_DTO(state, data) {
    // 기존 콘텐츠
    let content = state.commentDto.content;
    const newContent = data.content.filter((comment) => {
      return comment.mainCommentId === null
    });
    content.push(...newContent);

    // 중복 제거
    let set = new Set(content.map(JSON.stringify));
    content = Array.from(set).map(JSON.parse);

    state.commentDto = data;
    state.commentDto.content = content;
  }
}