<template>
  <section ref="section">
    <div class="title" ref="title">MANAGER 채팅방</div>
    <div class="chat_room" ref="chat_room">
      <infinite-loading direction="top" :identifier="infiniteId" @infinite="infiniteHandler" spinner="spiral" slot="append" force-use-infinite-wrapper=".chat_room">
        <span slot="no-more"></span>
        <span slot="no-results"></span>
      </infinite-loading>
      <div v-for="chat in chatDeque" :class="[ isSameUser(chat.writer) ? 'my_chat_box' : 'others_chat_box' ]" :key="chat.id">
        <span class="day_first" v-if="chat.dayFirst">
          {{ chat.fullDate }}
        </span>
        <div class="chat_box">
          <div class="icon" v-if="chat.writer !== currentUser">
            <i class="fas fa-user-circle"></i>
          </div>
          <div class="text_info">
            <div class="writer" v-if="!isSameUser(chat.writer)">{{ chat.writer }}</div>
            <div class="chat_text">{{ chat.text }}</div>
          </div>
          <div class="time">
            <div>{{ chat.date }}</div>
          </div>
        </div>
      </div>
    </div>
    <div class="write_box" ref="write_box">
      <contenteditable tag="span" class="input_text" v-model="text" @keyup.enter.exact="enterKeySendText"></contenteditable>
      <i class="fas fa-paper-plane" @click="sendText"></i>
    </div>
  </section>
</template>

<script>
import { getWebSocket } from '@/api';

export default {
  data() {
    const websocket = getWebSocket('/chat');
    const mql = window.matchMedia("screen and (max-width: 768px)");
    return {
      websocket,
      mql,
      isMobile: mql.matches,
      text: ''
    }
  },
  computed: {
    pageHeight () {
      return document.body.scrollHeight;
    },
    chatData() {
      return this.$store.state.chatDto;
    },
    currentUser() {
      return this.$store.state.userInfo.username;
    },
    chatDeque() {
      return this.$store.state.chatDeque.data;
    },
    infiniteId() {
      return this.$store.state.infiniteId;
    },
    page() {
      return this.$store.state.page;
    }
  },
  created() {
    // 무한 스크롤 사용을 위해서
    this.$store.state.page = 1;
    this.$store.state.infiniteId = +new Date();
  },
  mounted() {
    const dom = this.$refs.chat_room;
    dom.scrollTop = dom.scrollHeight - dom.clientHeight;

    this.initWebsocket();
  },
  methods: {
    initWebsocket() {
      this.websocket.onopen = () => {
        console.log("chat is connected.");
      }
      this.websocket.onclose = () => {
        console.log("chat is closed.");
      }
      this.websocket.onmessage = async evt => {
        if (evt.data !== '201') return;

        await this.$store.dispatch('GET_CHAT_DATA');
        // 스크롤 마지막 위치로 다시 이동
        const dom = this.$refs.chat_room;
        dom.scrollTop = dom.scrollHeight - dom.clientHeight;

        // 무한 스크롤 다시 작동
        this.$store.state.page = 1;
        this.$store.state.infiniteId += 1;
      }
      this.websocket.onerror = evt => {
        console.warn("ERROR: " + evt.data);
      }
    },
    async sendText() {
      if (this.text !== '') {
        // 매니저 권한이 있는지 먼저 검증하기위해 호출
        await this.$store.dispatch('FETCH_DATA', '/manager');
        await this.$store.dispatch('FETCH_DATA', '/manager');

        if (!this.$store.getters.unauthorized) {
          const writer = this.$store.state.userInfo.username;
          this.websocket.send(JSON.stringify({ writer, text: this.text }));
          this.text = '';

          const dom = this.$refs.chat_room;
          dom.scrollTop = dom.scrollHeight - dom.clientHeight;
        } else {
          await this.$router.push("/");
        }
      }
    },
    async enterKeySendText() {
      if (!this.isMobile) {
        await this.sendText();
      }
    },
    isSameUser(writer) {
      return this.currentUser === writer;
    },
    async infiniteHandler($state) {
      await this.$store.dispatch('INSERT_CHAT_DATA', this.page);

      if (this.chatData.last) {
        $state.complete();
      } else {
        this.$store.state.page += 1;
        $state.loaded();
      }
    }
  },
  beforeDestroy() {
    this.websocket.close();
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

.same_time {
  opacity: 0;
}

.day_first {
  align-self: center;
  font-size: 0.688rem;
  margin: 0.564rem;
  padding: 5px 12px;
  border-radius: 20px;
  background-color: #9aacbb;
  color: #e9eff3;
}

// 기본이 모바일
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
    display: flex;
    flex-direction: column;
    .others_chat_box {
      display: flex;
      flex-direction: column;
      .chat_box {
        display: flex;
        margin: 0.282rem;
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
    }
    .my_chat_box {
      display: flex;
      flex-direction: column;
      .chat_box {
        display: flex;
        flex-direction: row-reverse;
        margin: 0.282rem;
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

@media screen and (max-width: 30rem) {
  section {
    height: calc(100vh - 3.45rem)
  }
}

// 데스크탑
@media screen and (min-width: 48rem) {
  section {
    height: calc(100vh - 66.97px - 60px);
    display: flex;
    flex-direction: column;
    justify-content: center;
    width: 500px;
    margin: 30px auto;
    border: 10px solid;
    border-radius: 2%;
  }
}
</style>