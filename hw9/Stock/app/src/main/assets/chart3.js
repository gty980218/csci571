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
    xhr.open("GET","https://stock-gty218-hw8.wl.r.appspot.com/getTrends/"+Android.getTicker(),true);
    xhr.send();
}

function loadChart(response){

    

    let strongBuy=[];
    let buy=[];
    let hold=[];
    let sell=[];
    let strongSell=[];
    let period=[]
    
    for (i = 0; i < response.length; i += 1) {
            strongBuy.push(response[i]["strongBuy"]);
            buy.push(response[i]["buy"]);
            hold.push(response[i]["hold"]);
            sell.push(response[i]["sell"]);
            strongSell.push(response[i]["strongSell"]);
            period.push(response[i]["period"]);
        }
    console.log(period);
    
    Highcharts.chart('container', {
      chart: {
        type: 'column',
    },
    title: {
        text: 'Recommendation Trends'
    },
    xAxis: {
        categories: period,
        labels: {
            style: {
                fontSize: '10px',
                }
            }
    },
    yAxis: {
        min: 0,
        title: {
            text: '#Analysis'
        },
        stackLabels: {
            enabled: true,
            style: {
                fontWeight: 'bold',
                color: ( // theme
                    Highcharts.defaultOptions.title.style &&
                    Highcharts.defaultOptions.title.style.color
                ) || 'gray'
            }
        }
    },
    legend: {
        align: 'center',
        x: 0,
        verticalAlign: 'bottom',
        y: 0,
        floating: false,
        backgroundColor:
            Highcharts.defaultOptions.legend.backgroundColor || 'white',
        borderColor: '#CCC',
        borderWidth: 1,
        shadow: false,
    },
    tooltip: {
        headerFormat: '<b>{point.x}</b><br/>',
        pointFormat: '{series.name}: {point.y}<br/>Total: {point.stackTotal}'
    },
    plotOptions: {
        column: {
            stacking: 'normal',
            dataLabels: {
                enabled: false
            }
        }
    },
    series: [{
          name: 'Stong Buy',
          type: 'column',
          data: strongBuy,
          color:'darkgreen'
      }, {
          name: 'Buy',
          type: 'column',
          data: buy,
          color:'#4CDF7D'
      }, {
          name: 'Hold',
          type: 'column',
          data: hold,
          color:'#906716'
      }, {
          name: 'Sell',
          type: 'column',
          data: sell,
          color:'#DC5454'
      }, {
          name: 'Strong Sell',
          type: 'column',
          data: strongSell,
          color:'#6A1212'
      },]
    });
};