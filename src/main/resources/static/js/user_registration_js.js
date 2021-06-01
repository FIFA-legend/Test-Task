let checkEmail = function (input, error) {
    if (input.value.length <= 0) {
        error.innerHTML = "Not Empty";
        return false;
    } else {
        error.innerHTML = "";
        return true;
    }
}

let checkLength = function (input, error) {
    if (input.value.length < 8 || input.value.length > 32) {
        error.innerHTML = "Length from 8 to 32 symbols";
        return false;
    } else {
        error.innerHTML = "";
        return true;
    }
}

let checkRepeat = function (input, inputToCompare, error) {
    if (input.value !== inputToCompare.value) {
        error.innerHTML = "Password mismatch";
        return false;
    } else {
        error.innerHTML = "";
        return true;
    }
}

let showButton = function (flag1, flag2, flag3, flag4, button) {
    button.disabled = !(flag1 && flag2 && flag3 && flag4);
}

let usernameError = document.getElementById("username_errors");
let passwordError = document.getElementById("password_errors");
let repeatError = document.getElementById("repeat_errors");
let emailError = document.getElementById("email_errors");

let usernameInput = document.getElementById("username");
let passwordInput = document.getElementById("password");
let repeatInput = document.getElementById("repeat");
let emailInput = document.getElementById("email");
let button = document.getElementById("button");

let isUsernameCorrect = checkLength(usernameInput, usernameError);
let isPasswordCorrect = checkLength(passwordInput, passwordError);
let isRepeatCorrect = checkRepeat(passwordInput, repeatInput, repeatError);
let isEmailCorrect = checkEmail(emailInput, emailError);
showButton(isUsernameCorrect, isPasswordCorrect, isRepeatCorrect, isEmailCorrect, button);

usernameInput.addEventListener("keyup", function () {
    isUsernameCorrect = checkLength(usernameInput, usernameError);
    showButton(isUsernameCorrect, isPasswordCorrect, isRepeatCorrect, isEmailCorrect, button);
});


passwordInput.addEventListener("keyup", function () {
    isPasswordCorrect = checkLength(passwordInput, passwordError);
    isRepeatCorrect = checkRepeat(passwordInput, repeatInput, repeatError);
    showButton(isUsernameCorrect, isPasswordCorrect, isRepeatCorrect, isEmailCorrect, button);
});


repeatInput.addEventListener("keyup", function () {
    isRepeatCorrect = checkRepeat(passwordInput, repeatInput, repeatError);
    showButton(isUsernameCorrect, isPasswordCorrect, isRepeatCorrect, isEmailCorrect, button);
});

emailInput.addEventListener("keyup", function () {
    isEmailCorrect = checkEmail(emailInput, emailError);
    showButton(isUsernameCorrect, isPasswordCorrect, isRepeatCorrect, isEmailCorrect, button);
});