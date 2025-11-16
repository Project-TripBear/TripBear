// resources/js/main.js

document.addEventListener('DOMContentLoaded', function() {

    // 공통으로 쓸 클래스 이름
    const OPEN_CLASS = 'open';

    // --- 1. 모바일 메뉴 패널 열고 닫기 ---
    const hamburgerBtn = document.getElementById('hamburger-btn');
    const mobileMenu   = document.getElementById('mobile-menu');
    const closeMenuBtn = document.getElementById('close-menu-btn');

    if (hamburgerBtn && mobileMenu && closeMenuBtn) {
        // 열기
        hamburgerBtn.addEventListener('click', function () {
            mobileMenu.classList.add(OPEN_CLASS);   // ★ active → open
        });

        // 닫기
        closeMenuBtn.addEventListener('click', function () {
            mobileMenu.classList.remove(OPEN_CLASS); // ★ active → open
        });

        // 패널 바깥(오버레이) 클릭 시 닫기 원하면 이거 추가해도 됨
        mobileMenu.addEventListener('click', function (e) {
            if (e.target === mobileMenu) {
                mobileMenu.classList.remove(OPEN_CLASS);
            }
        });
    }

    // --- 2. 모바일 사이드바 드롭다운 (여행정보 / 게시판) ---
    const dropdownToggles = document.querySelectorAll(
        '.mobile-nav-links .has-dropdown > .dropdown-toggle'
    );

    dropdownToggles.forEach(function (toggle) {
        toggle.addEventListener('click', function (e) {
            e.preventDefault();

            const parentItem = toggle.closest('.has-dropdown');
            const subMenu    = parentItem.querySelector('.mobile-sub-menu');

            if (!parentItem || !subMenu) return;

            // 현재 열려 있던 상태였는지 체크
            const wasOpen = parentItem.classList.contains(OPEN_CLASS);

            // 1) 모든 드롭다운 닫기
            document.querySelectorAll('.mobile-nav-links .has-dropdown.' + OPEN_CLASS)
                .forEach(function (item) {
                    item.classList.remove(OPEN_CLASS);
                    const innerMenu = item.querySelector('.mobile-sub-menu');
                    if (innerMenu) {
                        innerMenu.style.display = 'none';
                    }
                });

            // 2) 원래 닫혀 있던 경우만 다시 열기
            if (!wasOpen) {
                parentItem.classList.add(OPEN_CLASS);
                subMenu.style.display = 'block';
            }
        });
    });

}); // DOMContentLoaded
