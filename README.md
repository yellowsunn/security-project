# security-project
## 개요
* 스프링 시큐리티를 이용하여 DB와 연동한 커스텀한 인증, 인가 서비스를 제공한다.
  * Remember Me, CSRF 방지 토큰, 동시 세션 수 제한 기능 사용
* 부가 기능으로 게시판, 실시간 채팅방, 사용자 관리 기능을 구현
* backend: `spring-boot`, frontend: `Vue.js`
  * 각각의 프로젝트는 Docker image로 배포함
    * https://hub.docker.com/r/yellowsunn/security-project-backend
    * https://hub.docker.com/r/yellowsunn/security-project-frontend
* 전체 서비스는 docker compose 명령어로 테스트 해볼 수 있다.
  ```bash
  git clone https://github.com/yellowsunn/security-project.git
  cd security-project
  docker-compose up
  ```
  
## Demo
http://yellowsunn.duckdns.org/
* 기본 ADMIN 계정 (이 계정은 수정,삭제가 불가능함)
  * id: `root`
  * password: `yellowsunn`
  
