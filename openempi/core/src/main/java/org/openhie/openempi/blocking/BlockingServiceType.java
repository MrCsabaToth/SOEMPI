/**
 * 
 *  Copyright (C) 2013 Vanderbilt University <csaba.toth, b.malin @vanderbilt.edu>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.openhie.openempi.blocking;

import java.io.Serializable;

import org.openhie.openempi.model.BaseObject;
import org.openhie.openempi.blocking.BlockingService;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

public class BlockingServiceType extends BaseObject implements Serializable
{
	private static final long serialVersionUID = 872971699370322668L;

	private String name;
	private BlockingService blockingService;
	
	public BlockingServiceType(String name, BlockingService blockingService) {
		this.name = name;
		this.blockingService = blockingService;
	}

	public BlockingService getBlockingService() {
		return blockingService;
	}

	public void setBlockingService(BlockingService blockingService) {
		this.blockingService = blockingService;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof BlockingServiceType))
			return false;
		BlockingServiceType castOther = (BlockingServiceType) other;
		return new EqualsBuilder().append(name, castOther.name).append(
				blockingService, castOther.blockingService).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(name).append(blockingService)
				.toHashCode();
	}

	@Override
	public String toString() {
		return name;
	}
}
