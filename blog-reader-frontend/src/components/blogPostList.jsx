import React, {Component} from "react";
import BlogPostSelector from "./blogPostSelector";

/**
 * Represents a list of Blogposts and renders them as BlogPostSelector Components
 */
export default class BlogPostList extends Component{

    render(){
        return (
            <React.Fragment>
                {this.props.blogPosts.map(blogPost =>
                    <BlogPostSelector
                        key={blogPost.id}
                        blogPost={blogPost}
                        handleSelection={this.props.handleSelection}
                        index = {this.props.blogPosts.indexOf(blogPost)}
                    />)}
            </React.Fragment>
        )
    }
}