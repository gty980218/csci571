function loadChart(response){
    let actual=[];
    let estimate=[];
    let date=[]
    for(i=0;i<response.length;i++){
      actual.push(response[i]["actual"]);
      estimate.push(response[i]["estimate"]);
      date.push(response[i]["period"]+"<br>surprises:"+response[i]["surprise"]);
    }
    
    Highcharts.chart('container', {
       chart: {
        
      },
      title: {
          text: 'Historical EPS Surprises'

      },
      xAxis: {
          categories: date,
          labels: {
            style: {
                fontSize: '10px',
                }
            }
      },
      yAxis: {
          title: {
              text: 'Quarterly EPS'
          }
      },
      tooltip: {
          shared: true,
      },

      series: [{
          name: 'Actural',
          type:'line',
          data: actual,
      }, {
          name: 'Estimate',
          type:'line',
          data: estimate
      }]
    });
};

function sendReq(){
    var xhr=new XMLHttpRequest();
    xhr.onreadystatechange=function(){
        if(xhr.readyState==4 && xhr.status==200){
            var json=JSON.parse(xhr.responseText);
            loadChart(json);
        }else{
        }
    }
    const currentDate = new Date(); 
    const timestamp = currentDate. getTime();
    console.log(parseInt(timestamp/1000));
    xhr.open("GET","https://stock-gty218-hw8.wl.r.appspot.com/getEarnings/"+Android.getTicker(),true);
    xhr.send();
}

