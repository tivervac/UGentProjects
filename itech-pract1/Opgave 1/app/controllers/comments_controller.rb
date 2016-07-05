class CommentsController < ApplicationController
  def create
    @event = Event.find(params[:event_id])

   # if !params[:comment].has_key?(:person_id)
    #  redirect_to event_path(@event)
   # else
      @comment = @event.comments.create(comment_params)
      redirect_to event_path(@event)
   # end
  end

  def edit
    @comment = Comment.find(params[:id])
  end

  def update
    @event = Event.find(params[:event_id])
    @comment = Comment.find(params[:id])

    if @comment.update(comment_params)
      redirect_to @event
    else
      render 'edit'
    end
  end

  def destroy
    @event = Event.find(params[:event_id])
    @comment = Comment.find(params[:id])
    @comment.destroy

    redirect_to @event
  end

  private

  def comment_params
    params.require(:comment).permit(:body, :person_id)
  end
end
