$(function(){ // on dom ready

var cy = cytoscape({
  container: document.getElementById('cy'),
  
  style: [
    {
      selector: 'node',
      css: {
        'content': 'data(id)',
        'text-valign': 'center',
        'text-halign': 'center'
      }
    },
    {
      selector: 'edge',
      css: {
        'target-arrow-shape': 'triangle'
      }
    },
    {
      selector: ':selected',
      css: {
        'background-color': 'red',
        'line-color': 'red',
        'target-arrow-color': 'red',
        'source-arrow-color': 'red'
      }
    }
  ],
  
  elements: {
    nodes: [
      { data: { id: 'thread_Root' } },
      { data: { id: 'task.z.line_5.id_1', parent: 'thread_Root' } },
    ],
    edges: [
      { data: { id: 'None-thread_Root', source: 'None', target: 'thread_Root' } },
      { data: { id: 'threadid-threadid', source: 'threadid', target: 'threadid' } },
      { data: { id: 'taskid-taskid', source: 'taskid', target: 'taskid' } },
    ]
  },
  
  layout: {
    name: 'breadthfirst',
  }
});

}); // on dom ready

