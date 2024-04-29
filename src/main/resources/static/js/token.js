const token = serchParam('token')

if(token){
  localStorage.setItem("access_token", token)
}

function serchParam(key){
  return new URLSearchParams(location.search).get(key);
}