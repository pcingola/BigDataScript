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
      { data: { id: 'task.line_10.id_112', parent: 'thread_Root' } },
      { data: { id: 'task.line_14.id_113', parent: 'thread_Root' } },
    ],
    edges: [
      { data: { id: 'None-thread_Root', source: 'None', target: 'thread_Root' } },
      { data: { id: 'task.line_10.id_112-task.line_14.id_113', source: 'task.line_10.id_112', target: 'task.line_14.id_113' } },
      { data: { id: 'task.line_10.id_112-task.line_14.id_113', source: 'task.line_10.id_112', target: 'task.line_14.id_113' } },
      { data: { id: 'task.line_10.id_112-task.line_14.id_113', source: 'task.line_10.id_112', target: 'task.line_14.id_113' } },
      { data: { id: 'task.line_10.id_112-task.line_14.id_113', source: 'task.line_10.id_112', target: 'task.line_14.id_113' } },
      { data: { id: 'id-id', source: 'id', target: 'id' } },
    ]
  },
  
  layout: {
    name: 'breadthfirst',
  }
});

}); // on dom ready
