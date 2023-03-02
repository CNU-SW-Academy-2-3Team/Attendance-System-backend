const url = new URL(window.location.href);
const urlParams = url.searchParams;
const roomId = urlParams.get('id');

const $roomInfo = document.querySelector('.roominfo');    
const roomInfo_API = axios.get(`https://a247ba36-c96c-4c9c-8eb4-59be066afde6.mock.pstmn.io/roomlist/${roomId}`);
roomInfo_API
.then((e) => {
  
      
    
  $roomInfo.innerHTML +=  `
    <div class="room">
    <h3>${e.data.name}</h3>
    <p>${e.data.price}</p>
    <p>${e.data.seller}</p>
    </div>
    `
      
  
  console.log(e.data)
      
    
  })
  .catch((err) => {
      console.log(err.message)
    })
