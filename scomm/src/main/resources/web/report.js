/**
 * Created by Zsolt on 3/17/14.
 */
function loadXMLDoc(filename)
{
    var xhttp=new XMLHttpRequest();
    xhttp.open("GET",filename,false);
    xhttp.send();
    return xhttp.responseXML;
}

var executionNodes = [];

function pushNode(parentId, call, args, result) {
    var nodeId = executionNodes.length;
    var executionNode = {
        nodeId: nodeId,
        parentNodeId: parentId,
        call: call,
        args: args,
        result: result
    };
    executionNodes.push(executionNode);
    return executionNode;
}

function buildExecutions(parentId, execution) {
    var call = execution.children[0].innerHTML;
    var args = execution.children[1].innerHTML;
    var result = execution.children[2].innerHTML;
    var executionNode = pushNode(parentId, call, args, result);
    var invocations = execution.children[3];
    if (invocations != null) {
        for (var i = 0; i < invocations.children.length; i++) {
            var invocation = invocations.children[i];
            buildExecutions(executionNode.nodeId, invocation);
        }
    }
}

//function pad(num, size) {
//    var s = "000000000" + num;
//    return s.substr(s.length - size);
//}
//
//for (var reportIndex = 0; ; reportIndex++) {
//    var xml = loadXMLDoc("xml-reports/report_" + pad(reportIndex, 4) + ".xml");
//    if (xml == null) {
//        break;
//    }
//    addReport(xml);
//}

function addReport(xml) {
    var from = executionNodes.length;
    buildExecutions(-1, xml.documentElement);
    var to = executionNodes.length;
    var table = document.getElementById("example-basic-expandable");

    var rows = [];
    for (var i = from ; i < to; i++) {
        var executionNode = executionNodes[i];

        var row = table.insertRow(-1);
        row.setAttribute("data-tt-id", executionNode.nodeId);
        if (executionNode.parentNodeId >= 0) {
            row.setAttribute("data-tt-parent-id", executionNode.parentNodeId);
        }

        var callCell = row.insertCell(0);
        callCell.innerHTML = executionNode.call;

        var argCell = row.insertCell(1);
        argCell.innerHTML = executionNode.args;

        var resultCell = row.insertCell(2);
        resultCell.innerHTML = executionNode.result;

        rows.push(row);
    }
    $("#example-basic-expandable").treetable('loadBranch', null, rows);
}

$("#example-basic-expandable").treetable({ expandable: true });