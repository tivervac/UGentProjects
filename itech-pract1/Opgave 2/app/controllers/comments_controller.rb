class CommentsController < ApplicationController
  def create
    @event = Event.find(params[:event_id])

    respond_to  do |format|
      if params[:comment].has_key?(:person_id) && params[:comment].has_key?(:body)
        @comment = @event.comments.create(comment_params)
        format.json { render json: event_url(@event), status: :created }
      else
        format.json { render json: @event.errors, status: :bad_request }
      end
      format.html { redirect_to event_path(@event) }
    end
  end

  def edit
    @comment = Comment.find(params[:id])
  end

  def update
    @event = Event.find(params[:event_id])
    @comment = Comment.find(params[:id])

    respond_to do |format|
      if @comment.update(comment_params)
        format.html { redirect_to @event }
        format.json { render json: event_url(@event), status: :accepted }
      else
        format.html { render 'edit' }
        format.json { render json: @event.errors, status: :bad_request }
      end
    end
  end

  def destroy
    @event = Event.find(params[:event_id])
    @comment = Comment.find(params[:id])
    @comment.destroy

    respond_to do |format|
      format.html { redirect_to @event }
      format.json { render json: event_url(@event), status: :accepted }
    end
  end

  private

  def comment_params
    params.require(:comment).permit(:body, :person_id)
  end
end
