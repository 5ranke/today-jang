const emailCopyButton = document.getElementById("emailCopyButton");
const emailCopyStatus = document.getElementById("emailCopyStatus");

emailCopyButton?.addEventListener("click", async () => {
    const email = "wjdtnwls0106@gmail.com";

    try {
        await navigator.clipboard.writeText(email);

        if (emailCopyStatus) {
            emailCopyStatus.textContent = "복사됨";

            setTimeout(() => {
                emailCopyStatus.textContent = "";
            }, 1500);
        }
    } catch (error) {
        alert("이메일 주소를 복사하지 못했습니다.");
    }
});