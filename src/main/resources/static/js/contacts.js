console.log("contacts.js");
//const baseURL = "http://localhost:8081";
const baseURL = "https://scm77.ap-south-1.elasticbeanstalk.com";
const viewContactModal = document.getElementById("view_contact_modal");

// options with default values
const options = {
  placement: "bottom-right",
  backdrop: "dynamic",
  backdropClasses: "bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40",
  closable: true,
  onHide: () => {
    console.log("modal is hidden");
  },
  onShow: () => {
    setTimeout(() => {
      contactModal.classList.add("scale-100");
    }, 50);
  },
  onToggle: () => {
    console.log("modal has been toggled");
  },
};

// instance options object
const instanceOptions = {
  id: "view_contact_modal",
  override: true,
};

const contactModal = new Modal(viewContactModal, options, instanceOptions);

function openContactModal() {
  contactModal.show();
}

function closeContactModal() {
  contactModal.hide();
}

async function loadContactdata(id) {
  //function call to load data
  console.log(id);
  try {
    const data = await (await fetch(`${baseURL}/api/contacts/${id}`)).json();
    console.log(data);
    document.querySelector("#contact_name").innerHTML = data.name;
    document.querySelector("#contact_email").innerHTML = data.email;
    document.querySelector("#contact_image").src = data.picture;
    document.querySelector("#contact_address").innerHTML = data.address;
    document.querySelector("#contact_phone").innerHTML = data.phoneNumber;
    document.querySelector("#contact_about").innerHTML = data.description;
    const contactFavorite = document.querySelector("#contact_favorite");
    if (data.favorite) {
      contactFavorite.innerHTML =
        "<i class='fas fa-star text-red-400'></i>";
    } else {
      contactFavorite.innerHTML = "Not Favorite Contact";
    }

    document.querySelector("#contact_Instagram").href = data.InstagramLink;
    document.querySelector("#contact_Instagram").innerHTML = data.InstagramLink;
    document.querySelector("#contact_LinkedIn").href = data.LinkedInLink;
    document.querySelector("#contact_LinkedIn").innerHTML = data.LinkedInLink;
    document.querySelector("#contact_Facebook").href = data.FacebookInLink;
    document.querySelector("#contact_Facebook").innerHTML = data.FacebookInLink;
    document.querySelector("#contact_Twitter").href = data.TwitterLink;
    document.querySelector("#contact_Twitter").innerHTML = data.TwitterLink;
    openContactModal();
  } catch (error) {
    console.log("Error: ", error);
  }
}

// delete contact

async function deleteContact(id) {
    Swal.fire({
        title: "Do you want to delete this contact ?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Delete"
      }).then((result) => {
        if (result.isConfirmed) {
          const url = `${baseURL}/user/contacts/delete/${id}`;
          window.location.replace(url);
        }
  });
}