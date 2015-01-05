DynamicRecyclerView
===================

Set of light and plugable extenstions for Android RecyclerView widget. Does not use custom RecyclerView or LayoutManager.
With this extensions you can create RecyclerView with following features:

###Drag and drop RecyclerView items reordering. 
- Implemented using RecyclerView.OnItemTouchListener
- Very light, ~350 LOC
- Support for custom "Drag frame" drawable
 
#####Usage:
```java
    dragDropTouchListener = new DragDropTouchListener(recyclerView, this) {
        @Override
        protected void onItemSwitch(RecyclerView recyclerView, int from, int to) {
            adapter.swapPositions(from, to);
            adapter.notifyItemChanged(to);
            adapter.notifyItemChanged(from);
 
         @Override
         protected void onItemDrop(RecyclerView recyclerView, int position) {
        }
   };
   }
   
    recyclerView.addOnItemTouchListener(dragDropTouchListener);
```
###Swipe to dismiss. 
This is port of Roman Nurik's SwipeToDismiss https://github.com/romannurik/Android-SwipeToDismiss but for RecyclerView and using RecyclerView.OnItemTocuchListener
- Implemented using RecyclerView.OnItemTouchListener
- Configurable swipe directions: only left, only right, both, none
- ~320 LOC 

#####Usage:
```java
 swipeToDismissTouchListener = new SwipeToDismissTouchListener(recyclerView, new SwipeToDismissTouchListener.DismissCallbacks() {
            @Override
           public SwipeToDismissTouchListener.SwipeDirection canDismiss(int position) {
               return SwipeToDismissTouchListener.SwipeDirection.RIGHT;
           }
            @Override
           public void onDismiss(RecyclerView view, List<SwipeToDismissTouchListener.PendingDismissData> dismissData) {
              for (SwipeToDismissTouchListener.PendingDismissData data : dismissData) {
                  adapter.removeItem(data.position);
                  adapter.notifyItemRemoved(data.position);
              }
           }
   });
  recyclerView.addOnItemTouchListener(swipeToDismissTouchListener);
```

###RecyclerViewAdapter
- Small RecyclerView.Adapter extension that can keep a state of selected/activated items
#####Usage
Use as normal RecyclerView.Adapter

###ItemTouchListenerAdapter
As RecyclerView does not have standard way to add click listeners to the items, this `RecyclerView.OnItemTouchListener` intercepts touch events and translates them to simple `onItemClick()` and `onItemLongClick()` callbacks.

#####Usage
```java
    recyclerView.addOnItemTouchListener(new ItemTouchListenerAdapter(recyclerView, this));
```


