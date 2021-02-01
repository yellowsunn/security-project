import { BoardDto } from '@/dto/BoardDto';
import { Deque } from '@/common/Deque';

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
    // state.postDto = new PostDto(data.title, data.content, data.writer, null, data.postTime, data.hit);
    state.postDto = data;
  },
  INIT_POST_DTO(state) {
    state.postDto = '';
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
  },
  SET_CHAT_DTO(state, data) {
    state.chatDto = data;
    state.chatSet = new Set();
    state.chatDeque = new Deque();
    for (let i = 0; i < state.chatDto.content.length; i++) {
      state.chatDeque.push_front(state.chatDto.content[i]); // 앞으로 쌓음
      state.chatSet.add(JSON.stringify(state.chatDto.content[i]));
    }
  },
  INSERT_CHAT_DTO(state, { data, isHistory }) {
    state.chatDto = data;
    for (let i = 0; i < state.chatDto.content.length; i++) {
      // 중복 되지 않는 경우에만
      if (!state.chatSet.has(JSON.stringify(state.chatDto.content[i]))) {
        if (!isHistory) {
          // 채팅창에 새로운 메세지가 들어온 경우
          state.chatDeque.push_back(state.chatDto.content[i]); // 뒤에 삽입
        } else {
          // 과거의 메세지를 스크롤을 올려 보는 경우
          state.chatDeque.push_front(state.chatDto.content[i]); // 앞에 삽입
        }
        state.chatSet.add(JSON.stringify(state.chatDto.content[i]));
      }
    }
  }
}