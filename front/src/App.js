

const $rooms = document.querySelector('.roomlist'); 
const roomList = axios.get("http://localhost:8080/api/user/3/groups/created");
roomList
.then((e) => {
  for(let i = 0; i < e.data.length; i++){
  $rooms.innerHTML +=  `
    <input type=button value=${e.data[i].invite_code}></input>
    `
  }   
  
  }).then(() => {
    const roomsSelected = document.querySelectorAll('.room');
    for(let i =0; i< roomsSelected.length; i++){
      roomsSelected[i].addEventListener('click',() =>{

        window.location.href = `room.html?id=${roomsSelected[i].dataset.id}`
    })
  }

  })
  .catch((err) => {
      console.log(err.message)
    })



axios.post('http://localhost:8080/api/group', 
    {
      
      'uid': 3,
      'group_title' : 'test',
      'group_detail' : 'testset'
    }

  )
  .then(function (response) {
    console.log(response);
  })
  .catch(function (error) {
    console.log(error);
  });
  
    
 
  
  
  
  