let urlEndpoint = 'http://api.tildp.shop/subscribe';
// let urlEndpoint = 'http://localhost:8080/subscribe';
let eventSource = new EventSource(urlEndpoint);

let domainURL= 'http://api.tildp.shop';
// let domainURL= 'http://localhost:8080';

eventSource.addEventListener("latestNews", function (event) {
    let articleData = JSON.parse(event.data);
    let title = articleData.tilTitle;
    let content = articleData.tilContent;
    $('#til_title').text(title);
    $('#til_content').text(content);
    $('#toast').css("display", "block");
});


function toast_close(){
    $('#toast').css("display", "none");
}


function login_check(options, originalOptions, jqXHR){
    if(localStorage.getItem('token')) {
        jqXHR.setRequestHeader('Authorization', 'Bearer ' + localStorage.getItem('token'));
    } else {
        alert("로그인 해주세요")
        location.href =  `/index.html`
    }
}


function sign_out() {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    alert('로그아웃!')
    window.location.href = "/"
}

function goback() {
    window.history.back();
}

function read_user() {
    $.ajax({
        type: "GET",
        url: `${domainURL}/user`,
        async: false,
        success: function (response) {
            let user_info = response;
            $('.user_id_append').text(user_info['username']);
            $('.user_nickname_append').text(user_info['nickname']);
            $('.user_profile_info_append').text(user_info['introduce']);
            $('.user_profile_pic_append').text(user_info['picture']);
            $('.github_id_append').text(user_info['github_id']);
            $('.user_id_append').val(user_info['username']);
            $('.user_nickname_append').val(user_info['nickname']);
            $('.user_profile_info_append').val(user_info['introduce']);
            $('.user_profile_pic_append').val(user_info['picture']);
            $('.user_profile_pic_real_append').attr('src', user_info['picture_real']);
            $('.github_id_append').val(user_info['github_id']);

            let github_id = user_info['github_id']
            if (github_id == null || github_id === ""){
                $('.github_id_tag').hide();

            }
            return user_info;
        }
    });

}
