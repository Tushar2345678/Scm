
let currentTheme = getTheme();

// Run only when DOM is fully loaded
document.addEventListener("DOMContentLoaded", () => {
  changeTheme();
});

// Set theme and setup toggle button
function changeTheme() {
  changePageTheme(currentTheme, "");

  const changeThemeButton = document.querySelector("#theme_change_button");

  if (!changeThemeButton) {
    console.warn("No #theme_change_button found in DOM");
    return;
  }

  // Set initial button text
  const span = changeThemeButton.querySelector("span");
  if (span) {
    span.textContent = currentTheme === "light" ? "Dark" : "Light";
  }

  // Toggle theme on click
  changeThemeButton.addEventListener("click", () => {
    const oldTheme = currentTheme;
    currentTheme = currentTheme === "dark" ? "light" : "dark";
    changePageTheme(currentTheme, oldTheme);
  });
}

// Save theme to localStorage
function setTheme(theme) {
  localStorage.setItem("theme", theme);
}

// Get theme from localStorage
function getTheme() {
  return localStorage.getItem("theme") || "light";
}

// Apply theme to page
function changePageTheme(theme, oldTheme) {
  setTheme(theme);

  const html = document.querySelector("html");
  if (oldTheme) {
    html.classList.remove(oldTheme);
  }
  html.classList.add(theme);

  // Update button label
  const changeThemeButton = document.querySelector("#theme_change_button");
  if (changeThemeButton) {
    const span = changeThemeButton.querySelector("span");
    if (span) {
      span.textContent = theme === "light" ? "Dark" : "Light";
    }
  }
}

console.log("JS is loading...");
