function setCharts(response){
        var ticker=Android.getTicker();
        let date=response["t"];
        let high=response["h"];
        let low=response["l"];
        let open=response["o"];
        let price=response["c"];
        let vol=response["v"];

        // split the data set into close and volume
        let volume = [], close = [], dataLength = date.length;
        let i;
        for (i = 0; i < dataLength; i += 1) {
            close.push([
                date[i]*1000, // the date
                open[i], // open
                high[i], // high
                low[i], // low
                price[i], // close
            ]);

            volume.push([
                 date[i]*1000, // the date
                 vol[i] // the volume
            ]);
        }
        console.log(close);
        groupingUnits = [[
            'week',                         // unit name
            [1]                             // allowed multiples
        ], [
            'month',
            [1, 2, 3, 4, 6]
        ]],

         Highcharts.stockChart('container', {

        rangeSelector: {
            selected: 2
        },

        title: {
            text:ticker+ ' Historical'
        },

        subtitle: {
            text: 'With SMA and Volume by Price technical indicators'
        },

        yAxis: [{
            startOnTick: false,
            endOnTick: false,
            labels: {
                align: 'right',
                x: -3
            },
            title: {
                text: 'OHLC'
            },
            height: '60%',
            lineWidth: 2,
            resize: {
                enabled: true
            }
        }, {
            labels: {
                align: 'right',
                x: -3
            },
            title: {
                text: 'Volume'
            },
            top: '65%',
            height: '35%',
            offset: 0,
            lineWidth: 2
        }],

        tooltip: {
            split: true
        },

        plotOptions: {
            series: {
                dataGrouping: {
                    units: groupingUnits
                }
            }
        },

        series: [{
            type: 'candlestick',
            name: ticker,
            id: ticker,
            zIndex: 2,
            data: close,
        }, {
            type: 'column',
            name: 'Volume',
            id: 'volume',
            data: volume,
            yAxis: 1
        }, {
            type: 'vbp',
            linkedTo: ticker,
            params: {
                volumeSeriesID: 'volume'
            },
            dataLabels: {
                enabled: false
            },
            zoneLines: {
                enabled: false
            }
        }, {
            type: 'sma',
            linkedTo: ticker,
            zIndex: 1,
            marker: {
                enabled: false
            }
        }]
    });
}

function sendReq(){
    var xhr=new XMLHttpRequest();
    xhr.onreadystatechange=function(){
        if(xhr.readyState==4 && xhr.status==200){
            var json=JSON.parse(xhr.responseText);
            setCharts(json);
        }else{
        }
    }
    const currentDate = new Date(); 
    const timestamp = currentDate. getTime();
    console.log(parseInt(timestamp/1000));
    xhr.open("GET","https://stock-gty218-hw8.wl.r.appspot.com/getChart/"+Android.getTicker()+"/"+parseInt(timestamp/1000),true);
    xhr.send();
}

