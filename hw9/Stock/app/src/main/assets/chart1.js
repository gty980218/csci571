function loadChart(json){
    const up=parseInt(Android.getFlag());
    var color='#18e002';
    if(up==0){
        color="#e31b1b";
    }
    

    let date=json["t"];
    let price=json["c"];
    let data=[];
    for (i = 0; i < date.length; i += 1) {
            data.push([
                date[i]*1000, 
                price[i] 
            ]);
        }
    console.log(data);
    // Create the chart
    Highcharts.stockChart('container', {
        title: {
        text:Android.getTicker()+' Hourly Price Variance',
        style: {
          color: "#8c8c8c",
        }
      },
      time: {
        useUTC: false
      },

      xAxis: {
        type: 'datetime',
        minRange: 4 * 60 * 1000,
        minTickInterval: 4 * 60 * 1000
      },

      rangeSelector: {
        enabled: false
      },

      navigator: {
        enabled:false
      },

      series: [{
        type: 'line',
        color: color,
        data:data,
        tooltip: {
          valueDecimals: 2
        },
        pointPlacement: 'on'
      }],
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
    xhr.open("GET","https://stock-gty218-hw8.wl.r.appspot.com/getSummaryChart/"+Android.getTicker()+"/"+parseInt(timestamp/1000),true);
    xhr.send();
}

