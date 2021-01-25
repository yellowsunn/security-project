<template>
  <section ref="section">
    <div class="title" ref="title">MANAGER 채팅방</div>
    <div class="chat_room" ref="chat_room">
      <div class="others_chat_box">
        <div class="icon">
          <i class="fas fa-user-circle"></i>
        </div>
        <div class="text_info">
          <div class="writer">조한국</div>
          <div class="chat_text">안녕하세요, 여기는 예제 채팅입니다. 반갑습니다.</div>
        </div>
        <div class="time">
          <div>오후 02:03</div>
        </div>
      </div>
      <div class="my_chat_box">
        <div class="text_info">
          <div class="chat_text">안녕하세요, 여기는 예제 채팅입니다.</div>
        </div>
        <div class="time">
          <div>오후 02:03</div>
        </div>
      </div>

      <div class="others_chat_box">
        <div class="icon">
          <i class="fas fa-user-circle"></i>
        </div>
        <div class="text_info">
          <div class="writer">조한국</div>
          <div class="chat_text">안녕하세요, 여기는 예제 채팅입니다. 반갑습니다.</div>
        </div>
        <div class="time">
          <div>오후 02:03</div>
        </div>
      </div>
      <div class="my_chat_box">
        <div class="text_info">
          <div class="chat_text">안녕하세요, 여기는 예제 채팅입니다.</div>
        </div>
        <div class="time">
          <div>오후 02:03</div>
        </div>
      </div>
      <div class="others_chat_box">
        <div class="icon">
          <i class="fas fa-user-circle"></i>
        </div>
        <div class="text_info">
          <div class="writer">조한국</div>
          <div class="chat_text">안녕하세요, 여기는 예제 채팅입니다. 반갑습니다.</div>
        </div>
        <div class="time">
          <div>오후 02:03</div>
        </div>
      </div>
      <div class="my_chat_box">
        <div class="text_info">
          <div class="chat_text">안녕하세요, 여기는 예제 채팅입니다.</div>
        </div>
        <div class="time">
          <div>오후 02:03</div>
        </div>
      </div>
      <div class="others_chat_box">
        <div class="icon">
          <i class="fas fa-user-circle"></i>
        </div>
        <div class="text_info">
          <div class="writer">조한국</div>
          <div class="chat_text">안녕하세요, 여기는 예제 채팅입니다. 반갑습니다.</div>
        </div>
        <div class="time">
          <div>오후 02:03</div>
        </div>
      </div>
      <div class="my_chat_box">
        <div class="text_info">
          <div class="chat_text">안녕하세요, 여기는 예제 채팅입니다.</div>
        </div>
        <div class="time">
          <div>오후 02:03</div>
        </div>
      </div>
      <div class="others_chat_box">
        <div class="icon">
          <i class="fas fa-user-circle"></i>
        </div>
        <div class="text_info">
          <div class="writer">조한국</div>
          <div class="chat_text">안녕하세요, 여기는 예제 채팅입니다. 반갑습니다.</div>
        </div>
        <div class="time">
          <div>오후 02:03</div>
        </div>
      </div>
      <div class="my_chat_box">
        <div class="text_info">
          <div class="chat_text">안녕하세요, 여기는 예제 채팅입니다.</div>
        </div>
        <div class="time">
          <div>오후 02:03</div>
        </div>
      </div>
    </div>
    <div class="write_box" ref="write_box">
      <span class="input_text" role="textbox" contenteditable></span>
      <i class="fas fa-paper-plane"></i>
    </div>
  </section>
</template>

<script>
export default {
  computed: {
    pageHeight () {
      return document.body.scrollHeight;
    }
  },
  mounted() {
    this.$refs.chat_room.scrollTop = this.pageHeight;
  }
};
</script>

<style lang="scss" scoped>
$background-color: #aabfcf;
$time-color: #5e6a73;
$my-chatbox-color: #fce300;

::-webkit-scrollbar {
  display: none;
}

@media screen and (max-width: 48rem) {
  section {
    background-color: $background-color;
    height: calc(100vh - 3.667rem);
    font-size: 1rem;
    .title {
      width: 100%;
      font-size: 1.250em;
      font-weight: bold;
      text-align: center;
      padding: 0.625rem;
    }
    .chat_room {
      height: calc(100% - 3.125rem - 3.125rem);
      overflow-y: scroll;
      padding: 0 0.5em;
      .others_chat_box {
        display: flex;
        margin-bottom: 0.563em;
        margin-right: 1em;
        .icon {
          display: flex;
          flex-direction: column;
          justify-content: flex-start;
          font-size: 2.313rem;
          i {
            border-radius: 50%;
            box-shadow: inset 0 0 0 0.188rem $background-color;
            color: #7ac9c0;
            background-color: #bde2e2;
          }
        }
        .text_info {
          margin-left: 0.625em;
          .writer {
            font-size: 0.813em;
            margin-bottom: 0.188rem;
            font-weight: 400;
            color: #111111;
          }
          .chat_text {
            background-color: white;
            padding: 0.5em 0.75em;
            border-radius: 1em;
          }
        }
        .time {
          display: flex;
          flex-direction: column;
          justify-content: flex-end;
          font-size: 0.688em;
          margin-left: 0.5rem;
          color: $time-color;
          white-space: nowrap;
        }
      }
      .my_chat_box {
        display: flex;
        flex-direction: row-reverse;
        margin-bottom: 0.563em;
        margin-left: 1em;
        .text_info {
          .chat_text {
            padding: 0.5em 0.75em;
            border-radius: 1em;
            background-color: $my-chatbox-color;
          }
        }
        .time {
          display: flex;
          flex-direction: column;
          justify-content: flex-end;
          font-size: 0.688rem;
          margin-right: 0.5rem;
          color: $time-color;
          white-space: nowrap;
        }
      }
    }
    .write_box {
      position: sticky;
      bottom: 0;
      width: 100%;
      min-height: 3.125rem;
      background-color: white;
      display: flex;
      .input_text {
        width: 100%;
        min-height: 3.125rem;
        max-height: 6.25rem;
        scroll-padding: 0.5rem;
        overflow-y: scroll;
        display: inline-block;
        padding: 0.875rem .5rem .5rem .5rem;
      }
      .fa-paper-plane {
        display: flex;
        align-items: center;
        background-color: $my-chatbox-color;
        padding: 0.375em 1.063em;
        cursor: pointer;
      }
    }
  }
}

@media screen and (max-width: 30rem) {
  section {
    height: calc(100vh - 3.45rem)
  }
}
</style>