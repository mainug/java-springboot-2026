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
