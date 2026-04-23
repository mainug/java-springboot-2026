const modal = document.getElementById("foodModal");

function openModal() {
  modal.classList.add("show");
}

function closeModal() {
  modal.classList.remove("show");
}

// 배경 클릭 시 닫기
window.addEventListener("click", function (e) {
  if (e.target === modal) closeModal();
});

// ESC 키 닫기
window.addEventListener("keydown", function (e) {
  if (e.key === "Escape") closeModal();
});

// 수정 모달 열기
function openEditModal(id) {
  // 서버에서 해당 음식 데이터를 가져옴
  fetch("/food/get/" + id)
    .then((response) => response.json())
    .then((food) => {
      // 1. 모달의 각 input에 값 채우기
      document.getElementById("name").value = food.name;
      document.getElementById("category").value = food.category;
      document.getElementById("rating").value = food.rating;
      document.getElementById("eatDate").value = food.eatDate;
      document.getElementById("memo").value = food.memo;

      // 2. 모달 제목 및 버튼 텍스트 변경
      document.querySelector(".modal-title").innerText = "음식 기록 수정하기";
      document.querySelector(".modal-subtitle").innerText =
        "기존 기록을 수정합니다.";

      // 3. 폼 제출 주소를 '등록(/food/add)'에서 '수정(/food/update/ID)'으로 변경
      const form = document.querySelector("#foodModal form");
      form.action = "/food/update/" + id;

      // 4. 모달 열기 (기존 함수 재사용)
      openModal();
    })
    .catch((error) => alert("데이터를 가져오는 중 오류가 발생했습니다."));
}

// 등록을 위한 새 모달 열기 (기존 함수 수정)
function openAddModal() {
  // 폼 초기화 (이전에 입력했던 내용 지우기)
  const form = document.querySelector("#foodModal form");
  form.reset();
  form.action = "/food/add"; // 다시 등록 주소로 설정

  document.querySelector(".modal-title").innerText = "먹은 음식 기록하기";
  document.querySelector(".modal-subtitle").innerText =
    "새로운 미식 기록을 추가합니다.";

  openModal();
}
